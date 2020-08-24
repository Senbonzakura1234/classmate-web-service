package com.app.manager.service.interfaceClass;

import com.app.manager.context.specification.CourseSpecification;
import com.app.manager.model.payload.CourseModel;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CourseService {
    Page<CourseModel> findAll(CourseSpecification courseSpecification, Pageable pageable);
    DatabaseQueryResult save(CourseModel courseModel);
    Optional<CourseModel> getOne(String id);
    DatabaseQueryResult update(CourseModel courseModel, String id);
    DatabaseQueryResult delete(String id);
}
