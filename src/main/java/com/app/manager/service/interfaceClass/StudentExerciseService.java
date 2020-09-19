package com.app.manager.service.interfaceClass;

import com.app.manager.model.payload.request.MarkExerciseRequest;
import com.app.manager.model.payload.request.StudentExerciseRequest;
import com.app.manager.model.payload.response.StudentExerciseResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;
import java.util.Optional;

public interface StudentExerciseService {
    DatabaseQueryResult saveStudentExercise(StudentExerciseRequest studentExerciseRequest,
                                            String currentUsername, String exerciseId);
    DatabaseQueryResult unSubmitStudentExercise(String currentUsername, String exerciseId);

    List<StudentExerciseResponse> getAllStudentExercise(String sessionId, String currentUsername);
    List<StudentExerciseResponse> getStudentExerciseOfOneStudentByCourse(String courseId, String currentUsername);
    Optional<StudentExerciseResponse> getStudentExercise(String studentExerciseId, String currentUsername);
    DatabaseQueryResult markExcercise(String exerciseId, String currentUsername,
                                      MarkExerciseRequest markExerciseRequest);
}
