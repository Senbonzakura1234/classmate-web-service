package com.app.manager.model.payload.response;

import java.util.List;

public class SessionAttendanceResponse {
    private SessionResponse sessionResponse;
    private List<AttendanceCheckResponse> attendanceCheckResponseList;

    public SessionAttendanceResponse() {
    }

    public SessionAttendanceResponse(SessionResponse sessionResponse,
                                     List<AttendanceCheckResponse> attendanceCheckResponseList) {
        this.sessionResponse = sessionResponse;
        this.attendanceCheckResponseList = attendanceCheckResponseList;
    }

    public SessionResponse getSessionResponse() {
        return sessionResponse;
    }

    public void setSessionResponse(SessionResponse sessionResponse) {
        this.sessionResponse = sessionResponse;
    }

    public List<AttendanceCheckResponse> getAttendanceCheckResponseList() {
        return attendanceCheckResponseList;
    }

    public void setAttendanceCheckResponseList(List<AttendanceCheckResponse> attendanceCheckResponseList) {
        this.attendanceCheckResponseList = attendanceCheckResponseList;
    }
}
