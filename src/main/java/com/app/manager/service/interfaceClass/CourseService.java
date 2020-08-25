package com.app.manager.service.interfaceClass;

import com.app.manager.context.specification.CourseSpecification;
import com.app.manager.model.payload.request.CourseRequest;
import com.app.manager.model.payload.response.CourseResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<CourseResponse> findAll(CourseSpecification courseSpecification);
    DatabaseQueryResult save(CourseRequest courseRequest, String currentUsername);
    Optional<CourseResponse> getOne(String id);
    DatabaseQueryResult update(CourseRequest courseRequest, String id, String currentUsername);
    DatabaseQueryResult delete(String id, String currentUsername);
}
