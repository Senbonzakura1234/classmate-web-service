package com.app.manager.model.payload.response;

public class GradeRecordResponse {
    private UserProfileResponse userProfileResponse;
    private boolean isNotNull;
    private StudentExerciseResponse studentExerciseResponse;


    public GradeRecordResponse() {
        isNotNull = false;
    }

    public GradeRecordResponse(UserProfileResponse userProfileResponse,
                               StudentExerciseResponse studentExerciseResponse) {
        this.userProfileResponse = userProfileResponse;
        this.studentExerciseResponse = studentExerciseResponse;
        isNotNull = true;
    }

    public UserProfileResponse getUserProfileResponse() {
        return userProfileResponse;
    }

    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        this.userProfileResponse = userProfileResponse;
    }

    public StudentExerciseResponse getStudentExerciseResponse() {
        return studentExerciseResponse;
    }

    public void setStudentExerciseResponse(StudentExerciseResponse studentExerciseResponse) {
        this.studentExerciseResponse = studentExerciseResponse;
    }

    public boolean isNotNull() {
        return isNotNull;
    }


    public void setNotNull(boolean notNull) {
        isNotNull = notNull;
    }
}
