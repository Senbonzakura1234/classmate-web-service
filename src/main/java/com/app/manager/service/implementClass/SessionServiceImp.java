package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.context.specification.SessionSpecification;
import com.app.manager.entity.*;
import com.app.manager.model.payload.CastObject;
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
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class SessionServiceImp implements SessionService {
    @Autowired SessionRepository sessionRepository;
    @Autowired UserRepository userRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired StudentCourseRepository studentCourseRepository;
    @Autowired AttendanceRepository attendanceRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired CastObject castObject;

    @Override
    public List<SessionResponse> findAll(SessionSpecification sessionSpecification) {
        try {
            List<Session> sessions = sessionRepository.findAll(sessionSpecification);
            return sessions.stream().map(session ->
                    castObject.sessionModelPublic(session))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }


    @Override
    public DatabaseQueryResult save(SessionRequest sessionRequest, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false,
                        "Teacher not found",
                        HttpStatus.NOT_FOUND, "");

            if(teacher.get().getSubscription() != ESubscription.PREMIUM
                    && sessionRequest.getSession_duration() >
                    teacher.get().getSubscription().getMax_session_duration())
                return new DatabaseQueryResult(false,
                        "Please upgrade your subcription for more session time",
                        HttpStatus.BAD_REQUEST, "");

            var course = courseRepository
                    .findById(sessionRequest.getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if(!course.getUser_id().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");
            var session = castObject.sessionEntity(sessionRequest);
            sessionRepository.save(session);
            return new DatabaseQueryResult(true,
                    "save session success", HttpStatus.OK,
                    castObject.sessionModel(session));
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save session failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public Optional<SessionResponse> getOne(String id, String currentUsername) {
        try {
            var currentUser = userRepository
                    .findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("user not found"));

            var role = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("role not found"));
            if(role.getStatus() == Role.StatusEnum.HIDE) return Optional.empty();
            var session = sessionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("session not found"));

            if (currentUser.getRoles().contains(role))
                return Optional.of(castObject.sessionModel(session));

            var course = courseRepository.findById(session.getCourse_id())
                    .orElseThrow(() -> new RuntimeException("course not found"));
            if(course.getUser_id().equals(currentUser.getId()))
            return Optional.of(castObject.sessionModel(session));

            var listStudents = studentCourseRepository
                    .findAllByCourse_idAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW);
            if (listStudents.stream().anyMatch(studentCourse ->
                    studentCourse.getUser_id().equals(currentUser.getId())))
                return Optional.of(castObject.sessionModel(session));
            return Optional.of(castObject.sessionModelPublic(session));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
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

            if(teacher.get().getSubscription() != ESubscription.PREMIUM
                    && sessionRequest.getSession_duration() >
                    teacher.get().getSubscription().getMax_session_duration())
                return new DatabaseQueryResult(false,
                        "Please upgrade your subcription for more session time",
                        HttpStatus.BAD_REQUEST, "");

            var s = sessionRepository.findById(id);
            if(s.isEmpty()){
                return new DatabaseQueryResult(false,
                        "save course failed", HttpStatus.NOT_FOUND, "");
            }

            var course = courseRepository
                    .findById(s.get().getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if(!course.getUser_id().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");

            if(course.getStatus() != Course.StatusEnum.ONGOING)
                return new DatabaseQueryResult(false, "Course is not ongoing",
                        HttpStatus.BAD_REQUEST, "");

            var session  = s.get();
            session.setSession_duration(sessionRequest.getSession_duration());
            session.setName(sessionRequest.getName());
            session.setStart_time(sessionRequest.getStart_time());
            session.setCourse_id(sessionRequest.getCourse_id());
            sessionRepository.save(session);
            return new DatabaseQueryResult(true,
                    "save session success", HttpStatus.OK,
                    castObject.sessionModel(session));
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save session failed", HttpStatus.INTERNAL_SERVER_ERROR,
                    sessionRequest);
        }
    }

    @Override
    public DatabaseQueryResult updateStatus(String id, Session.StatusEnum status,
                                            String currentUsername) {
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
                    .findById(session.get().getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            var role = roleRepository.findByName(ERole.ROLE_ADMIN);
            if(role.isEmpty() || role.get().getStatus() == Role.StatusEnum.HIDE)
                return new DatabaseQueryResult(false, "Role not found",
                        HttpStatus.NOT_FOUND, "");

            if (!teacher.get().getRoles().contains(role.get())) {
                if(!course.getUser_id().equals(teacher.get().getId()))
                    return new DatabaseQueryResult(false, "Not your course",
                            HttpStatus.BAD_REQUEST, "");

                if(status != Session.StatusEnum.CANCEL){
                    return new DatabaseQueryResult(false,
                            "You dont have authority to change session status",
                            HttpStatus.BAD_REQUEST, "");
                }
            }

            var s = session.get();
            s.setStatus(status);
            sessionRepository.save(s);
            return new DatabaseQueryResult(true,
                    "delete course success", HttpStatus.OK,
                    castObject.sessionModel(s));
        }catch (Exception e){
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public DatabaseQueryResult startAttendanceCheck(String id, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, "");

            if(!teacher.get().getSubscription().isAllow_face_check())
                return new DatabaseQueryResult(false,
                        "please upgrade to use this function",
                        HttpStatus.BAD_REQUEST, "");

            var session = sessionRepository.findById(id);
            if(session.isEmpty()) return new DatabaseQueryResult(false,
                    "Session not found", HttpStatus.NOT_FOUND, "");

            if(session.get().getAttendance_status() == Session.AttendanceStatusEnum.ONGOING)
                return new DatabaseQueryResult(false,
                "Session not found", HttpStatus.BAD_REQUEST, "");

            var course = courseRepository
                    .findById(session.get().getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if(!course.getUser_id().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");
            var s = session.get();
            s.setAttendance_check_start_time(System.currentTimeMillis());
            s.setUpdated_at(System.currentTimeMillis());
            s.setAttendance_status(Session.AttendanceStatusEnum.ONGOING);
            sessionRepository.save(s);

            var listStudentCourse = studentCourseRepository
                    .findAllByCourse_idAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW);
            var attendanceList = new ArrayList<Attendance>();
            listStudentCourse.forEach(studentCourse -> {
                var user = userRepository.findById(studentCourse.getUser_id());
                if(user.isEmpty()) return;
                var attendance = new Attendance();
                attendance.setUser_id(user.get().getId());
                attendance.setSession_id(s.getId());
                attendanceList.add(attendance);
            });
            if(!attendanceList.isEmpty()){
                attendanceRepository.saveAll(attendanceList);
            }
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

        if(!teacher.get().getSubscription().isAllow_face_check())
            return new DatabaseQueryResult(false,
                    "please upgrade to use this function",
                    HttpStatus.BAD_REQUEST, "");

        var session = sessionRepository.findById(id);
        if(session.isEmpty()){
            return new DatabaseQueryResult(false,
                    "Session not found", HttpStatus.NOT_FOUND, "");
        }

        var course = courseRepository
                .findById(session.get().getCourse_id())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if(!course.getUser_id().equals(teacher.get().getId()))
            return new DatabaseQueryResult(false, "Not your course",
                    HttpStatus.BAD_REQUEST, "");
        var s = session.get();
        s.setUpdated_at(System.currentTimeMillis());
        s.setAttendance_status(Session.AttendanceStatusEnum.END);
        sessionRepository.save(s);
        return new DatabaseQueryResult(true,
                "Attendance Closed", HttpStatus.OK, "");
    }
}
