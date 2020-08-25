package com.app.manager.service.interfaceClass;

import com.app.manager.context.specification.CourseSpecification;
import com.app.manager.model.payload.CourseModel;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<CourseModel> findAll(CourseSpecification courseSpecification);
    DatabaseQueryResult save(CourseModel courseModel);
    Optional<CourseModel> getOne(String id);
    DatabaseQueryResult update(CourseModel courseModel, String id, String currentUsername);
    DatabaseQueryResult delete(String id, String currentUsername);
}
