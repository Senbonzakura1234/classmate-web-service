package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;

public class JoinCourseByTokenRequest {
    @NotBlank
    private String token = "";

    public JoinCourseByTokenRequest() {
    }

    public JoinCourseByTokenRequest(@NotBlank String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
