package com.app.manager.context.repository;

import com.app.manager.entity.StudentExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentExerciseRepository extends JpaRepository<StudentExercise, String> {
}
