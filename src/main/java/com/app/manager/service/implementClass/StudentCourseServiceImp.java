package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.entity.*;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.StudentCourseRequest;
import com.app.manager.model.payload.response.CourseProfileResponse;
import com.app.manager.model.payload.response.SessionResponse;
import com.app.manager.model.payload.response.UserProfileResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.StudentCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;


@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class StudentCourseServiceImp implements StudentCourseService {
    @Autowired StudentCourseRepository studentCourseRepository;
    @Autowired UserRepository userRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired CastObject castObject;

    private static final Logger logger = LoggerFactory.getLogger(StudentCourseServiceImp.class);

    @Override
    public DatabaseQueryResult addStudentToCourse(
            StudentCourseRequest studentCourseRequest, String currentUsername) {

        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, studentCourseRequest);

            var course = courseRepository
                    .findById(studentCourseRequest.getCourse_id());
            if(course.isEmpty() || course.get().getStatus() == Course.StatusEnum.CANCEL)
                return new DatabaseQueryResult(false, "Course not found",
                        HttpStatus.NOT_FOUND, studentCourseRequest);

            if(!course.get().getUser_id().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not Your Course",
                        HttpStatus.BAD_REQUEST, studentCourseRequest);

            var role = roleRepository.findByName(ERole.ROLE_STUDENT);
            if(role.isEmpty() || role.get().getStatus() == Role.StatusEnum.HIDE)
                return new DatabaseQueryResult(false, "Role not found",
                        HttpStatus.NOT_FOUND, studentCourseRequest);

            var student = userRepository.findById(studentCourseRequest.getStudent_id());
            if(student.isEmpty() || !student.get().getRoles().contains(role.get()))
                return new DatabaseQueryResult(false, "Student not found," +
                        " or user is not Student",
                        HttpStatus.NOT_FOUND, studentCourseRequest);

//            if(student.get().isFacedefinition())
//                return new DatabaseQueryResult(false,
//                        "Student must have face definition",
//                        HttpStatus.BAD_REQUEST, "");


            var studentCourse = new StudentCourse();
            studentCourse.setCourse_id(studentCourseRequest.getCourse_id());
            studentCourse.setUser_id(studentCourseRequest.getStudent_id());
            studentCourse.setName(student.get().getUsername()
                    + ", " + course.get().getName());

            studentCourseRepository.save(studentCourse);

            return new DatabaseQueryResult(true, "add student to course success",
                    HttpStatus.OK, studentCourseRequest);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(false, "Server error",
                    HttpStatus.INTERNAL_SERVER_ERROR, studentCourseRequest);
        }
    }

    @Override
    public DatabaseQueryResult removeStudentFromCourse(StudentCourseRequest studentCourseRequest,
                                                       String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, studentCourseRequest);

            var course = courseRepository
                    .findById(studentCourseRequest.getCourse_id());
            if(course.isEmpty() || course.get().getStatus() == Course.StatusEnum.CANCEL)
                return new DatabaseQueryResult(false, "Course not found",
                        HttpStatus.NOT_FOUND, studentCourseRequest);

            if(!course.get().getUser_id().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not Your Course",
                        HttpStatus.BAD_REQUEST, studentCourseRequest);

            var studentCourse = studentCourseRepository
                .findFirstByCourse_idAndUser_idAndStatus(studentCourseRequest.getCourse_id(),
                    studentCourseRequest.getStudent_id(), StudentCourse.StatusEnum.SHOW);

            if(studentCourse.isEmpty())
                return new DatabaseQueryResult(false,
                        "student course not found",
                        HttpStatus.NOT_FOUND, studentCourseRequest);

            var sc = studentCourse.get();
            sc.setStatus(StudentCourse.StatusEnum.HIDE);
            sc.setDeleted_at(System.currentTimeMillis());
            sc.setUpdated_at(System.currentTimeMillis());
            studentCourseRepository.save(sc);

            return new DatabaseQueryResult(true,
                    "remove student from course success",
                    HttpStatus.OK, studentCourseRequest);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(false, "Server error",
                    HttpStatus.INTERNAL_SERVER_ERROR, studentCourseRequest);
        }
    }

    @Override
    public Optional<CourseProfileResponse> getAllProfileInCourse(String courseId) {
        try {
            var studentCourses = studentCourseRepository
                    .findAllByCourse_idAndStatus(courseId, StudentCourse.StatusEnum.SHOW)
                    .stream().map(StudentCourse::getUser_id).collect(Collectors.toList());
            var studentCoursesProfile =
                studentCourses.stream().map(s -> {
                try {
                    var user = userRepository.findById(s)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    return castObject.profilePublic(user);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return new UserProfileResponse();
                }
            }).filter(userProfileResponse -> userProfileResponse.getId() != null)
                .collect(Collectors.toList());

            var course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            var teacher = userRepository.findById(course.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            var currentSession = sessionRepository
                    .findFirstByCourse_idAndStatus(course.getId(),
                            Session.StatusEnum.ONGOING);
            var sessionModel = currentSession.isEmpty()? new SessionResponse() :
                    castObject.sessionModelPublic(currentSession.get());
            var studentCount = studentCourseRepository
                    .countAllByCourse_idAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW);
            var sessionCount = sessionRepository
                    .countAllByCourse_idAndStatusIsNot(course.getId(), Session.StatusEnum.CANCEL);
            return Optional.of(new CourseProfileResponse(castObject.courseModel(
                    course, sessionModel, studentCount, sessionCount),
                    castObject.profilePublic(teacher),
                    studentCoursesProfile));
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return Optional.empty();
        }
    }
}
