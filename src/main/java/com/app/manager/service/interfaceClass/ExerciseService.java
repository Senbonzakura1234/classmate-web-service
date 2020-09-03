package com.app.manager.service.interfaceClass;

import com.app.manager.context.specification.ExerciseSpecification;
import com.app.manager.entity.Exercise;
import com.app.manager.model.payload.request.ExerciseRequest;
import com.app.manager.model.payload.request.StudentExerciseRequest;
import com.app.manager.model.payload.response.ExerciseResponse;
import com.app.manager.model.payload.response.StudentExerciseResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;
import java.util.Optional;

public interface ExerciseService {
    List<ExerciseResponse> findAll(ExerciseSpecification exerciseSpecification);
    DatabaseQueryResult save(ExerciseRequest exerciseRequest, String currentUsername);
    Optional<ExerciseResponse> getOne(String id, String currentUsername);
    DatabaseQueryResult update(ExerciseRequest exerciseRequest, String id, String currentUsername);
    DatabaseQueryResult updateStatus(String id, Exercise.StatusEnum status, String currentUsername);

    DatabaseQueryResult saveStudentExercise(StudentExerciseRequest studentExerciseRequest,
                                            String currentUsername, String id);
    List<StudentExerciseResponse> getAllStudentExercise(String sessionId, String currentUsername);
    Optional<StudentExerciseResponse> getStudentExercise(String id, String currentUsername);
}
