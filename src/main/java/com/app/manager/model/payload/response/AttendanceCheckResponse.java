package com.app.manager.model.payload.response;

import com.app.manager.entity.Attendance;

public class AttendanceCheckResponse {
    private UserProfileResponse userProfileResponse;
    private String session_id;
    private Attendance.StatusEnum status;

    public AttendanceCheckResponse() {
    }

    public AttendanceCheckResponse(UserProfileResponse userProfileResponse,
                                   String session_id, Attendance.StatusEnum status) {
        this.userProfileResponse = userProfileResponse;
        this.session_id = session_id;
        this.status = status;
    }

    public UserProfileResponse getUserProfileResponse() {
        return userProfileResponse;
    }

    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        this.userProfileResponse = userProfileResponse;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public Attendance.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(Attendance.StatusEnum status) {
        this.status = status;
    }
}
