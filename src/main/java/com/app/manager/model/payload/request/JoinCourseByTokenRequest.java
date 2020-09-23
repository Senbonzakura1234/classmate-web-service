package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;

public class JoinCourseByTokenRequest {
    @NotBlank
    private String token = "";
    @NotBlank
    private String course_id = "";

    public JoinCourseByTokenRequest() {
    }

    public JoinCourseByTokenRequest(@NotBlank String token, @NotBlank String course_id) {
        this.token = token;
        this.course_id = course_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
}
