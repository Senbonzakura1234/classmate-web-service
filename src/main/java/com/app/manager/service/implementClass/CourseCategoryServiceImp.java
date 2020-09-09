package com.app.manager.service.implementClass;

import com.app.manager.context.repository.CourseCategoryRepository;
import com.app.manager.entity.CourseCategory;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.CourseCategoryRequest;
import com.app.manager.model.payload.response.CourseCategoryResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class CourseCategoryServiceImp implements CourseCategoryService {
    @Autowired
    CourseCategoryRepository coursecategoryRepository;
    @Autowired CastObject castObject;


    @Override
    public List<CourseCategoryResponse> getAll() {
        try {
            var courseCategories = (List<CourseCategory>)
                    coursecategoryRepository.findAll();
            return courseCategories.stream().map(courseCategory ->
                    castObject.courseCategoryModel(courseCategory))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public DatabaseQueryResult save(CourseCategoryRequest courseCategoryRequest) {
        try {
            coursecategoryRepository.save(
                    castObject.courseCategoryEntity(courseCategoryRequest));
            return new DatabaseQueryResult(
                    true, "save course category success",
                    HttpStatus.OK, courseCategoryRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(
                    false, "save course category failed",
                    HttpStatus.INTERNAL_SERVER_ERROR, courseCategoryRequest);
        }
    }

    @Override
    public DatabaseQueryResult update(String id,
                                      CourseCategoryRequest courseCategoryRequest) {
        try {
            var oldCourseCategory = coursecategoryRepository.findById(id);
            if(oldCourseCategory.isEmpty())
                return new DatabaseQueryResult(
                        false, "course category not found",
                        HttpStatus.NOT_FOUND, courseCategoryRequest);
            var cate = oldCourseCategory.get();
            cate.setName(courseCategoryRequest.getName());
            cate.setDescription(courseCategoryRequest.getDescription());
            coursecategoryRepository.save(cate);
            return new DatabaseQueryResult(
                    true, "save course category success",
                    HttpStatus.OK, courseCategoryRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(
                    false, "save course category failed",
                    HttpStatus.INTERNAL_SERVER_ERROR, courseCategoryRequest);
        }
    }
}
