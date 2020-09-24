package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.context.specification.SessionSpecification;
import com.app.manager.entity.*;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.SessionRequest;
import com.app.manager.model.payload.response.SessionResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired ExerciseRepository exerciseRepository;
    @Autowired AttendanceRepository attendanceRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired CastObject castObject;

    private static final Logger logger = LoggerFactory.getLogger(SessionServiceImp.class);

    @Override
    public List<SessionResponse> findAll(SessionSpecification sessionSpecification) {
        try {
            List<Session> sessions = sessionRepository.findAll(sessionSpecification);
            return sessions.stream().map(session ->
                    castObject.sessionModelPublic(session))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
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
                        HttpStatus.NOT_FOUND, sessionRequest);

            var course = courseRepository
                    .findById(sessionRequest.getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if(!course.getUser_id().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, sessionRequest);
            var session = castObject.sessionEntity(sessionRequest);
            sessionRepository.save(session);

            if(sessionRequest.isStart_immidiately()){
                updateStatus(session.getId(), Session.StatusEnum.ONGOING,
                    "", true);
                startExercise(session.getId());
            }
            return new DatabaseQueryResult(true,
                    "save session success", HttpStatus.OK,
                    castObject.sessionModel(session));
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save session failed",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    sessionRequest);
        }
    }

    @Override
    public Optional<SessionResponse> getOne(String sessionId, String currentUsername) {
        try {
            var currentUser = userRepository
                    .findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("user not found"));

            var role = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("role not found"));
            if(role.getStatus() == Role.StatusEnum.HIDE) return Optional.empty();
            var session = sessionRepository.findById(sessionId)
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
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return Optional.empty();
        }
    }

    @Override
    public DatabaseQueryResult update(SessionRequest sessionRequest,
                                      String sessionId, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, sessionRequest);

            var s = sessionRepository.findById(sessionId);
            if(s.isEmpty()) return new DatabaseQueryResult(false,
                    "save session failed",
                    HttpStatus.NOT_FOUND, sessionRequest);

            if(s.get().getStatus() != Session.StatusEnum.PENDING)
                return new DatabaseQueryResult(false,
                        "you can't update session when it is not pending",
                        HttpStatus.BAD_REQUEST, sessionRequest);


            var course = courseRepository
                    .findById(s.get().getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if(!course.getUser_id().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false,
                        "Not your course",
                        HttpStatus.BAD_REQUEST, sessionRequest);

            if(course.getStatus() != Course.StatusEnum.ONGOING)
                return new DatabaseQueryResult(false,
                        "Course is not ongoing",
                        HttpStatus.BAD_REQUEST, sessionRequest);

            var session  = s.get();
            session.setSession_duration(sessionRequest.getSession_duration());
            session.setName(sessionRequest.getName());
            session.setStart_time(sessionRequest.getStart_time());
            session.setCourse_id(sessionRequest.getCourse_id());
            sessionRepository.save(session);
            if(sessionRequest.isStart_immidiately()
                && session.getStatus() == Session.StatusEnum.PENDING){

                updateStatus(session.getId(), Session.StatusEnum.ONGOING,
                    "", true);

                startExercise(session.getId());
            }
            return new DatabaseQueryResult(true,
                    "save session success",
                    HttpStatus.OK, castObject.sessionModel(session));
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save session failed",
                    HttpStatus.INTERNAL_SERVER_ERROR, sessionRequest);
        }
    }

    @Override
    public DatabaseQueryResult updateStatus(String sessionId, Session.StatusEnum status,
                                            String currentUsername, boolean adminAuthority) {
        try {
            var session = sessionRepository.findById(sessionId);
            if(session.isEmpty()){
                return new DatabaseQueryResult(false,
                        "session not found", HttpStatus.NOT_FOUND, "");
            }
            if (!adminAuthority) {
                var teacher = userRepository.findByUsername(currentUsername);
                if(teacher.isEmpty())
                    return new DatabaseQueryResult(false, "Teacher not found",
                            HttpStatus.NOT_FOUND, "");


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

                    if(session.get().getStatus() == Session.StatusEnum.END
                        || session.get().getStatus() == Session.StatusEnum.CANCEL
                        || status == Session.StatusEnum.PENDING
                        || status == Session.StatusEnum.ALL){
                        return new DatabaseQueryResult(false,
                                "You dont have authority to change session status to "
                                        + status.getName(),
                                HttpStatus.BAD_REQUEST, "");
                    }
                }
            }

            var s = session.get();
            s.setStatus(status);
            if(status == Session.StatusEnum.ONGOING){
                s.setStart_time(System.currentTimeMillis());
                startExercise(s.getId());
            }
            sessionRepository.save(s);
            startAttendanceCheck(s.getId(), "", true);
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
    public DatabaseQueryResult startAttendanceCheck(String sessionId, String currentUsername,
                                                    boolean adminAuthority) {
        try {
            var session = sessionRepository.findById(sessionId);
            if(session.isEmpty()) return new DatabaseQueryResult(false,
                    "Session not found", HttpStatus.NOT_FOUND, "");

            if(session.get().getAttendance_status() == Session.AttendanceStatusEnum.ONGOING)
                return new DatabaseQueryResult(false,
                "Session not found", HttpStatus.BAD_REQUEST, "");

            var course = courseRepository
                    .findById(session.get().getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if (!adminAuthority) {
                var teacher = userRepository.findByUsername(currentUsername);
                if(teacher.isEmpty())
                    return new DatabaseQueryResult(false, "Teacher not found",
                            HttpStatus.NOT_FOUND, "");

                if(!course.getUser_id().equals(teacher.get().getId()))
                    return new DatabaseQueryResult(false, "Not your course",
                            HttpStatus.BAD_REQUEST, "");
            }

            var s = session.get();
            s.setUpdated_at(System.currentTimeMillis());
            s.setAttendance_status(Session.AttendanceStatusEnum.ONGOING);
            sessionRepository.save(s);

            var listStudentCourse = studentCourseRepository
                    .findAllByCourse_idAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW);

            listStudentCourse.forEach(studentCourse -> {
                try {
                    var user = userRepository.findById(studentCourse.getUser_id());
                    if(user.isEmpty()) return;
                    var attendance = new Attendance();
                    attendance.setUser_id(user.get().getId());
                    attendance.setSession_id(s.getId());
                    var oldAttendance = attendanceRepository
                            .findFirstByUser_idAndSession_id
                            (attendance.getUser_id(), attendance.getSession_id());
                    if(oldAttendance.isPresent()) return;
                    attendanceRepository.save(attendance);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info(e.getMessage());
                }
            });
            return new DatabaseQueryResult(true,
                    "Attendance Check Started", HttpStatus.OK, "");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(true,
                    "Fail to start Attendance Check",
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public DatabaseQueryResult closeAttendanceCheck(String sessionId, String currentUsername,
                                                    boolean adminAuthority) {
        try {
            var session = sessionRepository.findById(sessionId);
            if(session.isEmpty()){
                return new DatabaseQueryResult(false,
                        "Session not found", HttpStatus.NOT_FOUND, "");
            }

            var course = courseRepository
                    .findById(session.get().getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if (!adminAuthority) {
                var teacher = userRepository.findByUsername(currentUsername);
                if(teacher.isEmpty())
                    return new DatabaseQueryResult(false, "Teacher not found",
                            HttpStatus.NOT_FOUND, "");

                if(!course.getUser_id().equals(teacher.get().getId()))
                    return new DatabaseQueryResult(false, "Not your course",
                            HttpStatus.BAD_REQUEST, "");
            }

            var s = session.get();
            s.setUpdated_at(System.currentTimeMillis());
            s.setAttendance_status(Session.AttendanceStatusEnum.END);
            sessionRepository.save(s);
            return new DatabaseQueryResult(true,
                    "Attendance Closed", HttpStatus.OK, "");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(true,
                    "Fail to close Attendance Check",
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    private void startExercise(String sessionId){
        exerciseRepository.findAllBySession_idAndStatus
            (sessionId, Exercise.StatusEnum.PENDING)
            .forEach(exercise -> {
                if (exercise.isAuto_start()) {
                    try {
                        exercise.setStatus(Exercise.StatusEnum.ONGOING);
                        exerciseRepository.save(exercise);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info("Start Exercice failed");
                        logger.info("Error: " + e.getMessage());
                    }
                }
            });
    }
}
