package com.app.manager.service.interfaceClass;

import com.app.manager.context.specification.CourseSpecification;
import com.app.manager.entity.Course;
import com.app.manager.model.payload.request.CourseRequest;
import com.app.manager.model.payload.response.CourseResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<CourseResponse> findAll(CourseSpecification courseSpecification);
    List<CourseResponse> findAllByStudent(CourseSpecification courseSpecification, String currentUsername);
    List<CourseResponse> findAllByTeacher(CourseSpecification courseSpecification, String currentUsername);
    DatabaseQueryResult save(CourseRequest courseRequest, String currentUsername);
    Optional<CourseResponse> getOne(String courseId);
    DatabaseQueryResult update(CourseRequest courseRequest, String courseId, String currentUsername);
    DatabaseQueryResult updateStatus(String courseId, Course.StatusEnum status, String currentUsername);
}
