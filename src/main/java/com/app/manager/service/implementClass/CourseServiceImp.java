package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.context.specification.CourseSpecification;
import com.app.manager.entity.*;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.CourseRequest;
import com.app.manager.model.payload.response.CourseResponse;
import com.app.manager.model.payload.response.SessionResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.CourseService;
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
public class CourseServiceImp implements CourseService {
    @Autowired CourseRepository courseRepository;
    @Autowired StudentCourseRepository studentCourseRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired UserRepository userRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired CastObject castObject;
    
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImp.class);
    
    @Override
    public List<CourseResponse> findAll(CourseSpecification courseSpecification) {
        try {
            var courses = courseRepository.findAll(courseSpecification);
            return courses.stream().map(course -> {
                var currentSession = sessionRepository
                        .findFirstByCourse_idAndStatus(course.getId(),
                                Session.StatusEnum.ONGOING);
                var studentCount = studentCourseRepository
                        .countAllByCourse_idAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW);
                var sessionCount = sessionRepository
                        .countAllByCourse_idAndStatusIsNot(course.getId(), Session.StatusEnum.CANCEL);
                return castObject.courseModel(course, currentSession.isEmpty() ?
                        new SessionResponse() : castObject
                        .sessionModelPublic(currentSession.get()),
                        studentCount, sessionCount);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<CourseResponse> findAllByStudent(CourseSpecification courseSpecification,
                                                 String currentUsername) {
        try {
            var user = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("user not found"));

            var courses = courseRepository.findAll(courseSpecification)
                    .stream().filter(course -> studentCourseRepository
                    .findAllByUser_idAndStatus(user.getId(), StudentCourse.StatusEnum.SHOW).stream()
                    .anyMatch(studentCourse -> studentCourse.getCourse_id()
                            .equals(course.getId()))).collect(Collectors.toList());

            return courses.stream().map(course -> {
                var currentSession = sessionRepository
                        .findFirstByCourse_idAndStatus(course.getId(),
                                Session.StatusEnum.ONGOING);
                var studentCount = studentCourseRepository
                        .countAllByCourse_idAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW);
                var sessionCount = sessionRepository
                        .countAllByCourse_idAndStatusIsNot(course.getId(), Session.StatusEnum.CANCEL);
                return castObject.courseModel(course, currentSession.isEmpty() ?
                        new SessionResponse() : castObject
                        .sessionModelPublic(currentSession.get()), studentCount, sessionCount);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<CourseResponse> findAllByTeacher(CourseSpecification courseSpecification,
                                                 String currentUsername) {
        try {
            var user = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("user not found"));
            var courses = courseRepository.findAll(courseSpecification)
                    .stream().filter(course -> course.getUser_id().equals(user.getId()))
                    .collect(Collectors.toList());
            return courses.stream().map(course -> {
                var currentSession = sessionRepository
                        .findFirstByCourse_idAndStatus(course.getId(),
                                Session.StatusEnum.ONGOING);
                var studentCount = studentCourseRepository
                        .countAllByCourse_idAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW);
                var sessionCount = sessionRepository
                        .countAllByCourse_idAndStatusIsNot(course.getId(), Session.StatusEnum.CANCEL);
                return castObject.courseModel(course, currentSession.isEmpty() ?
                        new SessionResponse() : castObject
                        .sessionModelPublic(currentSession.get()), studentCount, sessionCount);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public DatabaseQueryResult save(CourseRequest courseRequest, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false,
                        "Teacher not found",
                        HttpStatus.NOT_FOUND, courseRequest);


            var course = castObject.courseEntity(courseRequest,
                    teacher.get().getId());
            courseRepository.save(course);

            var currentSession = sessionRepository
                    .findFirstByCourse_idAndStatus(course.getId(),
                            Session.StatusEnum.ONGOING);
            var studentCount = studentCourseRepository
                    .countAllByCourse_idAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW);
            var sessionCount = sessionRepository
                    .countAllByCourse_idAndStatusIsNot(course.getId(), Session.StatusEnum.CANCEL);

            return new DatabaseQueryResult(true,
                    "save course success",
                    HttpStatus.OK, castObject.courseModel(course,
                    currentSession.isEmpty()? new SessionResponse() :
                            castObject.sessionModelPublic(currentSession.get()),
                    studentCount, sessionCount));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());

            return new DatabaseQueryResult(false,
                    "save course failed",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    courseRequest);
        }
    }

    @Override
    public Optional<CourseResponse> getOne(String courseId) {
        try {
            var course = courseRepository.findById(courseId);
            if(course.isEmpty()) return Optional.empty();
            var currentSession = sessionRepository
                    .findFirstByCourse_idAndStatus(course.get().getId(),
                            Session.StatusEnum.ONGOING);
            var studentCount = studentCourseRepository
                    .countAllByCourse_idAndStatus(course.get().getId(), StudentCourse.StatusEnum.SHOW);
            var sessionCount = sessionRepository
                    .countAllByCourse_idAndStatusIsNot(course.get().getId(), Session.StatusEnum.CANCEL);
            return Optional.of(castObject.courseModel(course.get(),
                    currentSession.isEmpty()? new SessionResponse() :
                    castObject.sessionModelPublic(currentSession.get()), studentCount, sessionCount));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return Optional.empty();
        }
    }

    @Override
    public DatabaseQueryResult update(CourseRequest courseRequest,
                                      String courseId, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, courseRequest);

            var c = courseRepository.findById(courseId);
            if(c.isEmpty()) return new DatabaseQueryResult(false,
                    "save course failed",
                    HttpStatus.NOT_FOUND, courseRequest);
            if(c.get().getStatus() != Course.StatusEnum.PENDING)
                return new DatabaseQueryResult(false,
                        "you can't update your course when it is not pending",
                        HttpStatus.BAD_REQUEST, courseRequest);
            var course  = c.get();




            if(!course.getUser_id().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, courseRequest);

            course.setCoursecategory_id(courseRequest.getCourse_category_id());
            course.setDescription(courseRequest.getDescription());
            course.setCover_file_id(courseRequest.getCover_file_id());
            course.setEnd_date(courseRequest.getEnd_date());
            course.setName(courseRequest.getName());
            course.setStart_date(courseRequest.getStart_date());
            courseRepository.save(course);

            var currentSession = sessionRepository
                    .findFirstByCourse_idAndStatus(course.getId(),
                            Session.StatusEnum.ONGOING);
            var studentCount = studentCourseRepository
                    .countAllByCourse_idAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW);
            var sessionCount = sessionRepository
                    .countAllByCourse_idAndStatusIsNot(course.getId(), Session.StatusEnum.CANCEL);

            return new DatabaseQueryResult(true,
                    "save course success",
                    HttpStatus.OK, castObject.courseModel(course,
                    currentSession.isEmpty()? new SessionResponse() :
                    castObject.sessionModelPublic(currentSession.get()),
                    studentCount, sessionCount));
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    courseRequest);
        }
    }

    @Override
    public DatabaseQueryResult updateStatus(String courseId, Course.StatusEnum status,
                                            String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, "");

            var course = courseRepository.findById(courseId);
            if(course.isEmpty()){
                return new DatabaseQueryResult(false,
                        "update course failed", HttpStatus.NOT_FOUND, "");
            }

            var role = roleRepository.findByName(ERole.ROLE_ADMIN);
            if(role.isEmpty() || role.get().getStatus() == Role.StatusEnum.HIDE)
                return new DatabaseQueryResult(false, "Role not found",
                        HttpStatus.NOT_FOUND, "");

            if (!teacher.get().getRoles().contains(role.get())) {
                if(!course.get().getUser_id().equals(teacher.get().getId()))
                    return new DatabaseQueryResult(false, "Not your course",
                            HttpStatus.BAD_REQUEST, "");

                if(course.get().getStatus() == Course.StatusEnum.CANCEL ||
                    course.get().getStatus() == Course.StatusEnum.END ||
                    status == Course.StatusEnum.ALL ||
                    status == Course.StatusEnum.PENDING){
                    return new DatabaseQueryResult(false,
                            "You dont have authority to change course status to "
                                    + status.getName(),
                            HttpStatus.BAD_REQUEST, "");
                }
            }

            var c = course.get();
            c.setStatus(status);
            if(status == Course.StatusEnum.ONGOING){
                c.setStart_date(System.currentTimeMillis());
            }
            if(status == Course.StatusEnum.END){
                c.setEnd_date(System.currentTimeMillis());
            }
            courseRepository.save(c);
            return new DatabaseQueryResult(true,
                    "update course success", HttpStatus.OK,
                    "");

        }catch (Exception e){
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "update course failed",
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }
}
