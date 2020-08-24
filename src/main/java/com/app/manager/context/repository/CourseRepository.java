package com.app.manager.context.repository;

import com.app.manager.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseRepository extends JpaRepository<Course, String>,
        JpaSpecificationExecutor<Course> {

}
