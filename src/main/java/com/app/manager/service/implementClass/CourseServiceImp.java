package com.app.manager.service.implementClass;

import com.app.manager.context.repository.CourseRepository;
import com.app.manager.context.repository.UserRepository;
import com.app.manager.context.specification.CourseSpecification;
import com.app.manager.entity.Course;
import com.app.manager.model.payload.request.CourseRequest;
import com.app.manager.model.payload.response.CourseResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImp implements CourseService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    CourseRepository courseRepository;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    UserRepository userRepository;

    @Override
    public List<CourseResponse> findAll(CourseSpecification courseSpecification) {
        try {
            List<Course> courses = courseRepository.findAll(courseSpecification);
            List<CourseResponse> list = new ArrayList<>();
            courses.forEach(course -> list.add(new CourseResponse(
                    course.getId(),
                    course.getUserid(), course.getCoursecategoryid(),
                    course.getName(), course.getDescription(),
                    course.getStartdate(), course.getEnddate(),
                    course.getCreatedat(), course.getStatus())));
           return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public DatabaseQueryResult save(CourseRequest courseRequest, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false,
                        "Teacher not found", HttpStatus.NOT_FOUND, "");
            var course = CourseRequest.castToEntity(courseRequest,
                    teacher.get().getId());
            courseRepository.save(course);
            return new DatabaseQueryResult(true,
                    "save course success", HttpStatus.OK,
                    CourseResponse.castToObjectModel(course));
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public Optional<CourseResponse> getOne(String id) {
        try {
            var course = courseRepository.findById(id);
            if(course.isEmpty()){
                return Optional.empty();
            }
            return Optional.of(CourseResponse.castToObjectModel(course.get()));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public DatabaseQueryResult update(CourseRequest courseRequest,
                                      String id, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, "");

            var c = courseRepository.findById(id);
            if(c.isEmpty()){
                return new DatabaseQueryResult(false,
                        "save course failed", HttpStatus.NOT_FOUND, "");
            }

            var course  = c.get();

            if(!course.getUserid().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");

            course.setCoursecategoryid(courseRequest.getCoursecategoryid());
            course.setDescription(courseRequest.getDescription());
            course.setEnddate(courseRequest.getEnddate());
            course.setName(courseRequest.getName());
            course.setStartdate(courseRequest.getStartdate());
            courseRepository.save(course);

            return new DatabaseQueryResult(true,
                    "save course success", HttpStatus.OK,
                    CourseResponse.castToObjectModel(course));
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public DatabaseQueryResult delete(String id, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, "");

            var course = courseRepository.findById(id);
            if(course.isEmpty()){
                return new DatabaseQueryResult(false,
                        "delete course failed", HttpStatus.NOT_FOUND, "");
            }
            if(!course.get().getUserid().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");

            courseRepository.delete(course.get());
            return new DatabaseQueryResult(true,
                    "delete course success", HttpStatus.OK, "");
        }catch (Exception e){
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }
}
