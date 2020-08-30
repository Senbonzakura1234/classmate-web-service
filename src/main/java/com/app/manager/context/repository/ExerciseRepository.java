package com.app.manager.context.repository;

import com.app.manager.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExerciseRepository extends JpaRepository<Exercise, String>,
        JpaSpecificationExecutor<Exercise> {
}
