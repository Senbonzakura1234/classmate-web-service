package com.app.manager.service.implementClass;

import com.app.manager.context.repository.CourseRepository;
import com.app.manager.context.repository.RoleRepository;
import com.app.manager.context.repository.StudentCourseRepository;
import com.app.manager.context.repository.UserRepository;
import com.app.manager.entity.Course;
import com.app.manager.entity.ERole;
import com.app.manager.entity.Role;
import com.app.manager.entity.StudentCourse;
import com.app.manager.model.payload.request.StudentCourseRequest;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class StudentCourseServiceImp implements StudentCourseService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    StudentCourseRepository studentcourseRepository;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    UserRepository userRepository;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    CourseRepository courseRepository;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    RoleRepository roleRepository;


    @Override
    public DatabaseQueryResult addStudentToCourse(
            StudentCourseRequest studentCourseRequest, String currentUsername) {

        try {

            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, "");

            var course = courseRepository.findById(studentCourseRequest.getCourseId());
            if(course.isEmpty() || course.get().getStatus() == Course.StatusEnum.CANCEL)
                return new DatabaseQueryResult(false, "Course not found",
                        HttpStatus.NOT_FOUND, "");

            if(!course.get().getUserid().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not Your Course",
                        HttpStatus.BAD_REQUEST, "");

            var role = roleRepository.findByName(ERole.ROLE_STUDENT);
            if(role.isEmpty() || role.get().getStatus() == Role.StatusEnum.HIDE)
                return new DatabaseQueryResult(false, "Role not found",
                        HttpStatus.NOT_FOUND, "");

            var student = userRepository.findById(studentCourseRequest.getStudentId());
            if(student.isEmpty() || !student.get().getRoles().contains(role.get()))
                return new DatabaseQueryResult(false, "Student not found," +
                        " or user is not Student",
                        HttpStatus.NOT_FOUND, "");

//            if(student.get().isFacedefinition())
//                return new DatabaseQueryResult(false,
//                        "Student must have face definition",
//                        HttpStatus.BAD_REQUEST, "");


            var studentCourse = new StudentCourse();
            studentCourse.setCourseId(studentCourseRequest.getCourseId());
            studentCourse.setUserId(studentCourseRequest.getStudentId());

            studentcourseRepository.save(studentCourse);

            return new DatabaseQueryResult(true, "add student to course success",
                    HttpStatus.OK, studentCourseRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(false, "Server error",
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }
}
