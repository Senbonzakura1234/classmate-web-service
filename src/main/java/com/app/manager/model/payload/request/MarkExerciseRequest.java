package com.app.manager.model.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class MarkExerciseRequest {
    @Min(0) @Max(10) @NotNull
    private double mark;
    @NotNull
    private String teacher_message;

    public MarkExerciseRequest() {
    }

    public MarkExerciseRequest(@Min(0) @Max(10) @NotNull double mark,
                               @NotNull String teacher_message) {
        this.mark = mark;
        this.teacher_message = teacher_message;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public String getTeacher_message() {
        return teacher_message;
    }

    public void setTeacher_message(String teacher_message) {
        this.teacher_message = teacher_message;
    }
}
