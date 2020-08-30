package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.entity.Attendance;
import com.app.manager.entity.Session;
import com.app.manager.entity.StudentCourse;
import com.app.manager.entity.User;
import com.app.manager.model.payload.request.FaceCheckClientRequest;
import com.app.manager.model.payload.request.FaceCheckServerRequest;
import com.app.manager.model.payload.response.FaceCheckServerResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class AttendanceServiceImp implements AttendanceService {
    @Autowired AttendanceRepository attendanceRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired UserRepository userRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired StudentCourseRepository studentCourseRepository;

    private static final String faceCheckHost = "";

    @Override
    public DatabaseQueryResult studentAttendaneCheck
            (FaceCheckClientRequest faceCheckClientRequest,
            String currentUsername) {
        try {
            var session = sessionRepository
                    .findById(faceCheckClientRequest.getSession_id())
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            if(session.getStatus() == Session.StatusEnum.END
                    || session.getStatus() == Session.StatusEnum.PENDING)
                return new DatabaseQueryResult(false,
                        "Out of session time", HttpStatus.BAD_REQUEST,
                        faceCheckClientRequest);

            if(session.getAttendance_status() == Session.AttendanceStatusEnum.END
                    || session.getAttendance_status() == Session.AttendanceStatusEnum.PENDING)
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
            var faceCheckResult = faceCheck(faceCheckClientRequest, student);

            if(faceCheckResult.isEmpty() || !faceCheckResult.get().isMatched())
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
                newAttendance.setImage_uri(faceCheckClientRequest.getImg_url());
                newAttendance.setFace_matched(true);
                attendanceRepository.save(newAttendance);
                return new DatabaseQueryResult(true,
                        "Attendance Checked", HttpStatus.OK,
                        faceCheckClientRequest);
            }
            var a = attendance.get();
            a.setImage_uri(faceCheckClientRequest.getImg_url());
            a.setFace_matched(true);
            a.setUpdated_at(System.currentTimeMillis());
            attendanceRepository.save(a);

            return new DatabaseQueryResult(true,
                    "Attendance Checked", HttpStatus.OK,
                    faceCheckClientRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(false,
                    "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    private Optional<FaceCheckServerResponse> faceCheck
            (FaceCheckClientRequest faceCheckClientRequest, User user){
        try {
            var entity = new HttpEntity<>(new FaceCheckServerRequest(user.getFace_definition_id(),
                    faceCheckClientRequest.getImg_url()), new HttpHeaders());
            var restTemplate = new RestTemplate();

            var response = restTemplate
                    .exchange(faceCheckHost, HttpMethod.POST, entity, FaceCheckServerResponse.class);
            return response.getBody() != null ?
                    Optional.of(response.getBody()) : Optional.empty();
        } catch (RestClientException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }
}
