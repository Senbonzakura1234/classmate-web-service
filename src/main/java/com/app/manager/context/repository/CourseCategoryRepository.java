package com.app.manager.context.repository;

import com.app.manager.entity.CourseCategory;
import org.springframework.data.repository.CrudRepository;

public interface CourseCategoryRepository extends CrudRepository<CourseCategory, String> {
    CourseCategory findFirstByName(String name);
}
