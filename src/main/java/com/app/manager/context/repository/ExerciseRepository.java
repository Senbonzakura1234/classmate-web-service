package com.app.manager.context.repository;

import com.app.manager.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, String>,
        JpaSpecificationExecutor<Exercise> {
    List<Exercise> findAllBySession_idAndStatusIsNot(String session_id, Exercise.StatusEnum status);
    List<Exercise> findAllBySession_idAndStatus(String session_id, Exercise.StatusEnum status);
    List<Exercise> findAllBySession_idInAndStatusIsNot(List<String> session_id, Exercise.StatusEnum status);
}
