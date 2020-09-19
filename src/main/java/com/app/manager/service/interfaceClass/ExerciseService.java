package com.app.manager.service.interfaceClass;

import com.app.manager.context.specification.ExerciseSpecification;
import com.app.manager.entity.Exercise;
import com.app.manager.model.payload.request.ExerciseRequest;
import com.app.manager.model.payload.response.ExerciseResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;
import java.util.Optional;

public interface ExerciseService {
    List<ExerciseResponse> findAll(ExerciseSpecification exerciseSpecification);
    List<ExerciseResponse> gradeList(String courseId);
    List<ExerciseResponse> gradeListSingle(String courseId, String studentId);
    List<ExerciseResponse> gradeListStudent(String courseId, String currentUsername);
    DatabaseQueryResult save(ExerciseRequest exerciseRequest, String currentUsername, boolean adminAuthority);
    Optional<ExerciseResponse> getOne(String exerciseId, String currentUsername);
    DatabaseQueryResult update(ExerciseRequest exerciseRequest, String exerciseId, String currentUsername);
    DatabaseQueryResult updateStatus(String exerciseId, Exercise.StatusEnum status, String currentUsername);
}
