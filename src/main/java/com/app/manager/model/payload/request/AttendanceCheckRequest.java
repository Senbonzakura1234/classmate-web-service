package com.app.manager.model.payload.request;

import com.app.manager.entity.Attendance;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AttendanceCheckRequest {
    @NotBlank
    private String user_id;
    @NotNull
    private Attendance.StatusEnum status;

    public AttendanceCheckRequest() {
    }

    public AttendanceCheckRequest(@NotBlank String user_id,
                                  @NotNull Attendance.StatusEnum status) {
        this.user_id = user_id;
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Attendance.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(Attendance.StatusEnum status) {
        this.status = status;
    }
}
