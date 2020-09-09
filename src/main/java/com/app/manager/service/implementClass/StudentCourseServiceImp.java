package com.app.manager.service.implementClass;

import com.app.manager.context.repository.CourseRepository;
import com.app.manager.context.repository.RoleRepository;
import com.app.manager.context.repository.StudentCourseRepository;
import com.app.manager.context.repository.UserRepository;
import com.app.manager.entity.*;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.StudentCourseRequest;
import com.app.manager.model.payload.response.CourseProfileResponse;
import com.app.manager.model.payload.response.UserProfileResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;


@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class StudentCourseServiceImp implements StudentCourseService {
    @Autowired StudentCourseRepository studentcourseRepository;
    @Autowired UserRepository userRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired CastObject castObject;


    @Override
    public DatabaseQueryResult addStudentToCourse(
            StudentCourseRequest studentCourseRequest, String currentUsername) {

        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, studentCourseRequest);

            var course = courseRepository.findById(studentCourseRequest.getCourse_id());
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

            studentcourseRepository.save(studentCourse);

            return new DatabaseQueryResult(true, "add student to course success",
                    HttpStatus.OK, studentCourseRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(false, "Server error",
                    HttpStatus.INTERNAL_SERVER_ERROR, studentCourseRequest);
        }
    }

    @Override
    public Optional<CourseProfileResponse> getAllProfileInCourse(String courseId) {
        try {
            var studentCourses = studentcourseRepository
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
            }).collect(Collectors.toList());
            var course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            var teacher = userRepository.findById(course.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return Optional.of(new CourseProfileResponse(castObject.courseModel(course),
                    castObject.profilePublic(teacher), studentCoursesProfile ));
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }
}
