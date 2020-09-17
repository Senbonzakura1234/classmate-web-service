package com.app.manager.model.payload.response;

public class GradeRecordResponse {
    private UserProfileResponse userProfileResponse;
    private boolean exercisePosted;
    private final boolean isNull;
    private StudentExerciseResponse studentExerciseResponse;


    public GradeRecordResponse() {
        isNull = true;
    }

    public GradeRecordResponse(UserProfileResponse userProfileResponse) {
        this.userProfileResponse = userProfileResponse;
        exercisePosted = false;
        isNull = false;
    }

    public GradeRecordResponse(UserProfileResponse userProfileResponse,
                               StudentExerciseResponse studentExerciseResponse) {
        this.userProfileResponse = userProfileResponse;
        exercisePosted = true;
        this.studentExerciseResponse = studentExerciseResponse;
        isNull = false;
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

    public boolean isNull() {
        return isNull;
    }
}
