package com.app.manager.model.payload.response;

public class GradeRecordResponse {
    private UserProfileResponse userProfileResponse;
    private boolean exercisePosted;
    private final boolean isNotNull;
    private StudentExerciseResponse studentExerciseResponse;


    public GradeRecordResponse() {
        isNotNull = false;
    }

    public GradeRecordResponse(UserProfileResponse userProfileResponse) {
        this.userProfileResponse = userProfileResponse;
        exercisePosted = false;
        isNotNull = true;
    }

    public GradeRecordResponse(UserProfileResponse userProfileResponse,
                               StudentExerciseResponse studentExerciseResponse) {
        this.userProfileResponse = userProfileResponse;
        exercisePosted = true;
        this.studentExerciseResponse = studentExerciseResponse;
        isNotNull = true;
    }

    public boolean isExercisePosted() {
        return exercisePosted;
    }

    public void setExercisePosted(boolean exercisePosted) {
        this.exercisePosted = exercisePosted;
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
}
