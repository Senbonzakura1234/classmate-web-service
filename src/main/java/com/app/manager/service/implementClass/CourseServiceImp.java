package com.app.manager.service.implementClass;

import com.app.manager.entity.Course;
import com.app.manager.model.midware_model.CourseModel;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.repository.CourseRepository;
import com.app.manager.service.interfaceClass.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImp implements CourseService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    CourseRepository courseRepository;

    @Override
    public Page<CourseModel> getAll(String queryName, Course.StatusEnum status, Pageable pageable) {
        try {
            Page<Course> courses;
            if(queryName != null && !queryName.isEmpty()){
                if(status != null){
                    courses = courseRepository.findByNameContainsAndStatus(queryName, status, pageable);
                }else {
                    courses = courseRepository.findByNameContains(queryName, pageable);
                }
            }else {
                if(status != null){
                    courses = courseRepository.findByStatus(status, pageable);
                }else {
                    courses = courseRepository.findBy(pageable);
                }
            }
            return courses.map(course -> new CourseModel(course.getCoursecategoryid(), course.getName(), course.getDescription(), course.getStartdate(), course.getEnddate(), course.getCreatedat()));
        } catch (Exception e) {
            e.printStackTrace();
            return Page.empty();
        }
    }

    @Override
    public DatabaseQueryResult save(CourseModel courseModel) {
        try {
            courseRepository.save(CourseModel.castToEntity(courseModel));
            return new DatabaseQueryResult(true,
                    "save course success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<CourseModel> getOne(String id) {
        try {
            var course = courseRepository.findById(id);
            if(course.isEmpty()){
                return Optional.empty();
            }
            return Optional.of(CourseModel.castToObjectModel(course.get()));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public DatabaseQueryResult update(CourseModel courseModel, String id) {
        try {
            var c = courseRepository.findById(id);
            if(c.isEmpty()){
                return new DatabaseQueryResult(false,
                        "save course failed", HttpStatus.NOT_FOUND);
            }

            var course  = c.get();
            course.setCoursecategoryid(courseModel.getCoursecategoryid());
            course.setCreatedat(courseModel.getCreatedat());
            course.setDescription(courseModel.getDescription());
            course.setEnddate(courseModel.getEnddate());
            course.setName(courseModel.getName());
            course.setStartdate(courseModel.getStartdate());

            return new DatabaseQueryResult(true,
                    "save course success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public DatabaseQueryResult delete(String id) {
        try {
            var course = courseRepository.findById(id);
            if(course.isEmpty()){
                return new DatabaseQueryResult(false,
                        "delete course failed", HttpStatus.NOT_FOUND);
            }
            courseRepository.delete(course.get());
            return new DatabaseQueryResult(true,
                    "delete course success", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}