package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;

public class AttendanceCheckRequest {
    @NotBlank
    private String user_id;
    @NotBlank
    private String status;

    public AttendanceCheckRequest() {
    }

    public AttendanceCheckRequest(@NotBlank String user_id,
                                  @NotBlank String status) {
        this.user_id = user_id;
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
