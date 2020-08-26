package com.app.manager.context.repository;

import com.app.manager.entity.StudentCourse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentCourseRepository extends CrudRepository<StudentCourse, String> {
    List<StudentCourse> findAllByCourseIdAndStatus(String courseId, StudentCourse.StatusEnum status);
}
