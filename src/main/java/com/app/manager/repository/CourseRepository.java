package com.app.manager.repository;

import com.app.manager.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepository extends JpaRepository<Course, String> {
    Page<Course> findByNameContains(String queryName, Pageable pageable);
    Page<Course> findByNameContainsAndStatus(String queryName, Course.StatusEnum status, Pageable pageable);
    Page<Course> findByStatus(Course.StatusEnum status, Pageable pageable);
    Page<Course> findBy(Pageable pageable);
}