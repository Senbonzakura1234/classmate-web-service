package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.entity.Attendance;
import com.app.manager.entity.Session;
import com.app.manager.entity.StudentCourse;
import com.app.manager.entity.User;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.AttendanceCheckRequest;
import com.app.manager.model.payload.request.FaceCheckClientRequest;
import com.app.manager.model.payload.request.FaceCheckServerRequest;
import com.app.manager.model.payload.response.AttendanceCheckResponse;
import com.app.manager.model.payload.response.FaceCheckServerResponse;
import com.app.manager.model.payload.response.SessionAttendanceResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class AttendanceServiceImp implements AttendanceService {
    @Autowired AttendanceRepository attendanceRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired UserRepository userRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired StudentCourseRepository studentCourseRepository;
    @Autowired CastObject castObject;

    private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImp.class);
    private static final String faceCheckHost = "http://localhost:5000/api/checked";
    private static final double minimumMatchPercent = 0.8;

    @Override
    public DatabaseQueryResult studentAttendaneCheck
            (FaceCheckClientRequest faceCheckClientRequest,
            String currentUsername) {
        try {
            var session = sessionRepository
                    .findById(faceCheckClientRequest.getSession_id())
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            if(session.getStatus() != Session.StatusEnum.ONGOING)
                return new DatabaseQueryResult(false,
                        "Out of session time", HttpStatus.BAD_REQUEST,
                        faceCheckClientRequest);

            if(session.getAttendance_status() != Session.AttendanceStatusEnum.ONGOING)
                return new DatabaseQueryResult(false,
                        "Out of Attendance Check time", HttpStatus.BAD_REQUEST,
                        faceCheckClientRequest);

            var course = courseRepository.findById(session.getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            var studentCourses =
                    studentCourseRepository.findAllByCourse_idAndStatus(course.getId(),
                            StudentCourse.StatusEnum.SHOW);

            var student = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if(!student.isFace_definition() || student.getFace_definition_id() == null)
                return new DatabaseQueryResult(false,
                    "You dont have face definition," +
                            "get face definition first then try again",
                    HttpStatus.BAD_REQUEST,
                    faceCheckClientRequest);
            Optional<StudentCourse> result = studentCourses.stream()
                    .filter(studentCourse -> studentCourse.getUser_id()
                            .equals(student.getId())).findFirst();

            if(result.isEmpty())
                return new DatabaseQueryResult(false,
                    "You are not in this course", HttpStatus.BAD_REQUEST,
                    faceCheckClientRequest);

            // send request to host to face check

            var faceCheckResult =
                    faceCheck(faceCheckClientRequest, student);

            if(faceCheckResult.isEmpty() || faceCheckResult.get()
                    .getMatch_percent() < minimumMatchPercent)
                return new DatabaseQueryResult(false,
                    "Face not matched", HttpStatus.BAD_REQUEST,
                    faceCheckClientRequest);

            var attendance =
                    attendanceRepository.findFirstByUser_idAndSession_id(
                            student.getId(), session.getId());

            if(attendance.isEmpty()){
                var newAttendance = new Attendance();
                newAttendance.setSession_id(session.getId());
                newAttendance.setUser_id(student.getId());
                newAttendance.setFile_id(faceCheckClientRequest.getFile_id());
                newAttendance.setFace_matched(true);
                newAttendance.setStatus(Attendance.StatusEnum.ATTENDANT);
                attendanceRepository.save(newAttendance);
                return new DatabaseQueryResult(true,
                        "Attendance Checked", HttpStatus.OK,
                        faceCheckClientRequest);
            }
            var a = attendance.get();
            a.setFile_id(faceCheckClientRequest.getFile_id());
            a.setFace_matched(true);
            a.setStatus(Attendance.StatusEnum.ATTENDANT);
            a.setUpdated_at(System.currentTimeMillis());
            attendanceRepository.save(a);

            return new DatabaseQueryResult(true,
                    "Attendance Checked", HttpStatus.OK,
                    faceCheckClientRequest);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(false,
                    "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    faceCheckClientRequest);
        }
    }

    @Override
    public DatabaseQueryResult teacherAttendaneCheck(
            List<AttendanceCheckRequest> attendanceCheckRequests,
            String currentUsername, String sessionId, boolean adminAuthority) {
        try {
            var session = sessionRepository
                    .findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found"));
            var course = courseRepository
                    .findById(session.getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            if (!adminAuthority) {
                var currentUser = userRepository
                        .findByUsername(currentUsername)
                        .orElseThrow(() -> new RuntimeException("Session not found"));
                if(!course.getUser_id().equals(currentUser.getId()))
                    return new DatabaseQueryResult(false,
                            "Not your course",
                            HttpStatus.BAD_REQUEST, attendanceCheckRequests);
            }

            attendanceCheckRequests.forEach(attendanceCheckRequest -> {
                try {
                    if(attendanceCheckRequest.getStatus() == Attendance.StatusEnum.ALL ||
                     attendanceCheckRequest.getStatus() == Attendance.StatusEnum.NOT_SET)
                        return;
                    var studentCheck = studentCourseRepository
                            .findAllByCourse_idAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW)
                            .stream().noneMatch(studentCourse ->
                                    studentCourse.getUser_id().equals(attendanceCheckRequest.getUser_id()));

                    if (studentCheck) return;
                    var attendance =
                            attendanceRepository.findFirstByUser_idAndSession_id(
                                    attendanceCheckRequest.getUser_id(), sessionId);

                    if(attendance.isEmpty()){
                        var newAttendance = new Attendance();
                        newAttendance.setSession_id(sessionId);
                        newAttendance.setUser_id(attendanceCheckRequest.getUser_id());
                        newAttendance.setFile_id("");
                        newAttendance.setFace_matched(true);
                        newAttendance.setStatus(attendanceCheckRequest.getStatus());

                        attendanceRepository.save(newAttendance);
                        return;
                    }
                    var a = attendance.get();
                    a.setFile_id("");
                    a.setFace_matched(true);
                    a.setStatus(attendanceCheckRequest.getStatus());
                    a.setUpdated_at(System.currentTimeMillis());
                    attendanceRepository.save(a);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info(e.getMessage());
                    logger.info(e.getCause().getMessage());
                }
            });

            session.setAttendance_status(Session.AttendanceStatusEnum.END);
            sessionRepository.save(session);
            return new DatabaseQueryResult(true,
                    "Attendance Check Success",
                    HttpStatus.OK, attendanceCheckRequests);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(false,
                    "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    attendanceCheckRequests);
        }
    }

    @Override
    public DatabaseQueryResult teacherAttendaneCheckOne(AttendanceCheckRequest attendanceCheckRequest,
                                                        String currentUsername, String sessionId) {
        try {
            var currentUser = userRepository
                    .findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("Session not found"));
            var session = sessionRepository
                    .findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found"));
            var course = courseRepository
                    .findById(session.getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            var studentCheck = studentCourseRepository
                    .findAllByCourse_idAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW)
                    .stream().noneMatch(studentCourse ->
                            studentCourse.getUser_id().equals(attendanceCheckRequest.getUser_id()));

            if (studentCheck)
                return new DatabaseQueryResult(false,
                        "This student not in your course",
                        HttpStatus.BAD_REQUEST, attendanceCheckRequest);
            if(!course.getUser_id().equals(currentUser.getId()))
                return new DatabaseQueryResult(false,
                        "Not your course",
                        HttpStatus.BAD_REQUEST, attendanceCheckRequest);



            var attendance =
                    attendanceRepository.findFirstByUser_idAndSession_id(
                            attendanceCheckRequest.getUser_id(), sessionId);

            if(attendance.isEmpty()){
                var newAttendance = new Attendance();
                newAttendance.setSession_id(sessionId);
                newAttendance.setUser_id(attendanceCheckRequest.getUser_id());
                newAttendance.setFile_id("");
                newAttendance.setFace_matched(true);
                newAttendance.setStatus(attendanceCheckRequest.getStatus());

                attendanceRepository.save(newAttendance);
                return new DatabaseQueryResult(true,
                        "Attendance Check One Success",
                        HttpStatus.OK, attendanceCheckRequest);
            }
            var a = attendance.get();
            a.setFile_id("");
            a.setFace_matched(true);
            a.setStatus(attendanceCheckRequest.getStatus());
            a.setUpdated_at(System.currentTimeMillis());
            attendanceRepository.save(a);
            return new DatabaseQueryResult(true,
                    "Attendance Check One Success",
                    HttpStatus.OK, attendanceCheckRequest);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(false,
                    "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    attendanceCheckRequest);
        }
    }

    @Override
    public List<AttendanceCheckResponse> getAttendanceResult(String sessionId) {
        try {
            var list = attendanceRepository
                    .findAllBySession_idAndStatusIsNot(sessionId, Attendance.StatusEnum.ALL);
            return list.stream().map(attendance -> {
                var user = userRepository.findById(attendance.getUser_id());
                if(user.isEmpty()) return new AttendanceCheckResponse();
                return new AttendanceCheckResponse(castObject
                    .profilePublic(user.get()), attendance.getSession_id(),
                        attendance.getStatus());
            }).filter(attendanceCheckResponse ->
                attendanceCheckResponse.getSession_id() != null)
                .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<SessionAttendanceResponse> getListAttendanceResult(String courseId) {
        try {
            courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("course not found"));
            return sessionRepository
                .findAllByCourse_idAndStatusIsNot(courseId, Session.StatusEnum.CANCEL)
                .stream().map(session -> {
                    var checkResults =
                        getAttendanceResult(session.getId());
                    return new SessionAttendanceResponse(castObject
                        .sessionModel(session), checkResults);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new ArrayList<>();
        }
    }

    private Optional<FaceCheckServerResponse> faceCheck
            (FaceCheckClientRequest faceCheckClientRequest, User user){
        try {
            var entity = new HttpEntity<>(new FaceCheckServerRequest(user.getFace_definition_id(),
                    faceCheckClientRequest.getFile_id()), new HttpHeaders());
            var restTemplate = new RestTemplate();

            var response = restTemplate
                    .exchange(faceCheckHost, HttpMethod.POST, entity, FaceCheckServerResponse.class);
            return response.getBody() != null ?
                    Optional.of(response.getBody()) : Optional.empty();
        } catch (RestClientException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return Optional.empty();
        }
    }
}
