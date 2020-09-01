package com.app.manager.context.repository;

import com.app.manager.entity.StudentExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentExerciseRepository extends JpaRepository<StudentExercise, String> {
    Optional<StudentExercise> findFirstByUser_idAndExercise_id(String user_id, String exercise_id);
}
