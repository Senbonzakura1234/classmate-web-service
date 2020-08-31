package com.app.manager.context.repository;

import com.app.manager.entity.StudentCourse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StudentCourseRepository extends CrudRepository<StudentCourse, String> {
    List<StudentCourse> findAllByCourse_idAndStatus(String course_id, StudentCourse.StatusEnum status);
    List<StudentCourse> findAllByUser_idAndStatus(String user_id, StudentCourse.StatusEnum status);
    Optional<StudentCourse> findFirstByCourse_idAndUser_id(String course_id, String user_id);
}
