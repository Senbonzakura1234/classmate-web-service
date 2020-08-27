package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.context.specification.SessionSpecification;
import com.app.manager.entity.Attendance;
import com.app.manager.entity.Course;
import com.app.manager.entity.Session;
import com.app.manager.entity.StudentCourse;
import com.app.manager.model.payload.request.SessionRequest;
import com.app.manager.model.payload.response.SessionResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class SessionServiceImp implements SessionService {
    @Autowired SessionRepository sessionRepository;

    @Autowired UserRepository userRepository;

    @Autowired CourseRepository courseRepository;

    @Autowired StudentCourseRepository studentCourseRepository;

    @Autowired AttendanceRepository attendanceRepository;

    @Override
    public DatabaseQueryResult save(SessionRequest sessionRequest, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false,
                        "Teacher not found", HttpStatus.NOT_FOUND, "");

            var course = courseRepository
                    .findById(sessionRequest.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if(!course.getUserid().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");
            var session = SessionRequest.castToEntity(sessionRequest, currentUsername);
            sessionRepository.save(session);
            return new DatabaseQueryResult(true,
                    "save course success", HttpStatus.OK,
                    SessionResponse.castToObjectModel(session));
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public Optional<SessionResponse> getOne(String id) {
        try {
            var session = sessionRepository.findById(id);
            if(session.isEmpty()){
                return Optional.empty();
            }
            return Optional.of(SessionResponse.castToObjectModel(session.get()));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public DatabaseQueryResult update(SessionRequest sessionRequest,
                                      String id, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, "");

            var s = sessionRepository.findById(id);
            if(s.isEmpty()){
                return new DatabaseQueryResult(false,
                        "save course failed", HttpStatus.NOT_FOUND, "");
            }

            var course = courseRepository
                    .findById(s.get().getCourseid())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if(!course.getUserid().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");

            if(course.getStatus() != Course.StatusEnum.ONGOING)
                return new DatabaseQueryResult(false, "Course is not ongoing",
                        HttpStatus.BAD_REQUEST, "");

            var session  = s.get();
            session.setSessionduration(sessionRequest.getSessionduration());
            session.setName(sessionRequest.getName());
            session.setStarttime(sessionRequest.getStarttime());
            session.setCourseid(sessionRequest.getCourseId());
            sessionRepository.save(session);
            return new DatabaseQueryResult(true,
                    "save course success", HttpStatus.OK,
                    SessionResponse.castToObjectModel(session));
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR,
                    sessionRequest);
        }
    }

    @Override
    public DatabaseQueryResult delete(String id, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, "");

            var session = sessionRepository.findById(id);
            if(session.isEmpty()){
                return new DatabaseQueryResult(false,
                        "delete course failed", HttpStatus.NOT_FOUND, "");
            }

            var course = courseRepository
                    .findById(session.get().getCourseid())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if(!course.getUserid().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");

            sessionRepository.delete(session.get());
            return new DatabaseQueryResult(true,
                    "delete course success", HttpStatus.OK, "");
        }catch (Exception e){
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public List<SessionResponse> findAll(SessionSpecification sessionSpecification) {
        try {
            List<Session> sessions = sessionRepository.findAll(sessionSpecification);
            List<SessionResponse> list = new ArrayList<>();
            sessions.forEach(session -> list.add(new SessionResponse(session.getId(),
                    session.getCourseid(), session.getUserid(),
                    session.getName(), session.getStarttime(), session.getSessionduration(),
                    session.getAttendancestatus(), session.getStatus(), session.getCreatedat())));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public DatabaseQueryResult startAttendanceCheck(String id, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, "");

            var session = sessionRepository.findById(id);
            if(session.isEmpty()){
                return new DatabaseQueryResult(false,
                        "Session not found", HttpStatus.NOT_FOUND, "");
            }

            var course = courseRepository
                    .findById(session.get().getCourseid())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if(!course.getUserid().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");
            var s = session.get();
            s.setAttendancecheckstarttime(System.currentTimeMillis());
            s.setUpdatedat(System.currentTimeMillis());
            s.setAttendancestatus(Session.AttendanceStatusEnum.ONGOING);
            sessionRepository.save(s);

            var listStudentCourse = studentCourseRepository
                    .findAllByCourseIdAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW);
            var attendanceList = new ArrayList<Attendance>();
            listStudentCourse.forEach(studentCourse -> {
                var user = userRepository.findById(studentCourse.getUserId());
                if(user.isEmpty()) return;
                var attendance = new Attendance();
                attendance.setUserId(user.get().getId());
                attendance.setSessionId(s.getId());
                attendanceList.add(attendance);
            });
            attendanceRepository.saveAll(attendanceList);
            return new DatabaseQueryResult(true,
                    "Attendance Check Started", HttpStatus.OK, "");
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(true,
                    "Fail to start Attendance Check",
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public DatabaseQueryResult closeAttendanceCheck(String id, String currentUsername) {
        var teacher = userRepository.findByUsername(currentUsername);
        if(teacher.isEmpty())
            return new DatabaseQueryResult(false, "Teacher not found",
                    HttpStatus.NOT_FOUND, "");

        var session = sessionRepository.findById(id);
        if(session.isEmpty()){
            return new DatabaseQueryResult(false,
                    "Session not found", HttpStatus.NOT_FOUND, "");
        }

        var course = courseRepository
                .findById(session.get().getCourseid())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if(!course.getUserid().equals(teacher.get().getId()))
            return new DatabaseQueryResult(false, "Not your course",
                    HttpStatus.BAD_REQUEST, "");
        var s = session.get();
        s.setUpdatedat(System.currentTimeMillis());
        s.setAttendancestatus(Session.AttendanceStatusEnum.END);
        sessionRepository.save(s);
        return new DatabaseQueryResult(true,
                "Attendance Closed", HttpStatus.OK, "");
    }
}
