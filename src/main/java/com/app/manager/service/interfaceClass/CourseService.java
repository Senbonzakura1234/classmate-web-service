package com.app.manager.service.interfaceClass;

import com.app.manager.entity.Course;
import com.app.manager.model.midware_model.CourseModel;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Page<CourseModel> getAll(String queryName, Course.StatusEnum status, Pageable pageable);
    DatabaseQueryResult save(CourseModel courseModel);
    Optional<CourseModel> getOne(String id);
    DatabaseQueryResult update(CourseModel courseModel, String id);
    DatabaseQueryResult delete(String id);
}
