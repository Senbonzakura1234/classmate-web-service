package com.app.manager.model.payload.response;

import com.app.manager.entity.Attendance;

public class AttendanceCheckResponse {
    private String user_id;
    private String session_id;
    private Attendance.StatusEnum status;

    public AttendanceCheckResponse() {
    }

    public AttendanceCheckResponse(String user_id, String session_id,
                                   Attendance.StatusEnum status) {
        this.user_id = user_id;
        this.session_id = session_id;
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
