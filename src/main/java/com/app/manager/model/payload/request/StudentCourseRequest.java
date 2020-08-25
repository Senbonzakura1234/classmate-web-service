package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;

public class StudentCourseRequest {
    @NotBlank
    private String studentId;
    @NotBlank
    private String courseId;

    public StudentCourseRequest() {
    }

    public StudentCourseRequest(@NotBlank String studentId,
                                @NotBlank String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
