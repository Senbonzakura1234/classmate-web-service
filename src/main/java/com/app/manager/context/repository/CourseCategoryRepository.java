package com.app.manager.context.repository;

import com.app.manager.entity.CourseCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CourseCategoryRepository extends CrudRepository<CourseCategory, String> {
    Optional<CourseCategory> findFirstByName(String name);
}
