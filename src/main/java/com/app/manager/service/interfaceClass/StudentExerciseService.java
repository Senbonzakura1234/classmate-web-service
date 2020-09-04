package com.app.manager.service.interfaceClass;

import com.app.manager.model.payload.request.MarkExerciseRequest;
import com.app.manager.model.payload.request.StudentExerciseRequest;
import com.app.manager.model.payload.response.StudentExerciseResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;
import java.util.Optional;

public interface StudentExerciseService {
    DatabaseQueryResult saveStudentExercise(StudentExerciseRequest studentExerciseRequest,
                                            String currentUsername, String id);

    List<StudentExerciseResponse> getAllStudentExercise(String sessionId, String currentUsername);
    Optional<StudentExerciseResponse> getStudentExercise(String id, String currentUsername);
    DatabaseQueryResult markExcercise(String exerciseId, String currentUsername,
                                      MarkExerciseRequest markExerciseRequest);
}
