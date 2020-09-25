package com.app.manager.context.repository;

import com.app.manager.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String>,
        JpaSpecificationExecutor<Course> {
    Optional<Course> findFirstByName(String name);
    Optional<Course> findFirstByToken(String token);
}
