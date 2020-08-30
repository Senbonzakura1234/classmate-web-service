package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;

public class StudentCourseRequest {
    @NotBlank
    private String student_id;
    @NotBlank
    private String course_id;

    public StudentCourseRequest() {
    }

    public StudentCourseRequest(@NotBlank String student_id,
                                @NotBlank String course_id) {
        this.student_id = student_id;
        this.course_id = course_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
}
