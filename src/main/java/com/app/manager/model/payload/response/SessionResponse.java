package com.app.manager.model.payload.response;

import com.app.manager.entity.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionResponse {
    private String id;
    private String course_id; //search
    private String name; //search
    private String content;
    private Long start_time;
    private int session_duration;
    private Session.AttendanceStatusEnum attendance_status;
    private Session.StatusEnum status; //search
    private Long created_at;

    public SessionResponse(String id, String course_id, String name,
                           String content, Long start_time, int session_duration,
                           Session.AttendanceStatusEnum attendance_status,
                           Session.StatusEnum status, Long created_at) {
        this.id = id;
        this.course_id = course_id;
        this.name = name;
        this.content = content;
        this.start_time = start_time;
        this.session_duration = session_duration;
        this.attendance_status = attendance_status;
        this.status = status;
        this.created_at = created_at;
    }

    public SessionResponse() {
    }

    public static SessionResponse castToObjectModel(Session session){
        SessionResponse sessionResponse = new SessionResponse();
        if(session == null){
            return sessionResponse;
        }
        sessionResponse.setId(session.getId());
        sessionResponse.setCreated_at(session.getCreated_at());
        sessionResponse.setAttendance_status(session.getAttendance_status());
        sessionResponse.setSession_duration(session.getSession_duration());
        sessionResponse.setName(session.getName());
        sessionResponse.setContent(session.getContent());
        sessionResponse.setStart_time(session.getStart_time());
        sessionResponse.setStatus(session.getStatus());
        sessionResponse.setCourse_id(session.getCourse_id());

        return sessionResponse;
    }

    public static List<SessionResponse> castToListModels(List<Session> sessions){
        List<SessionResponse> sessionResponses = new ArrayList<>();
        if (sessions == null) return sessionResponses;
        for (var item: sessions) {
            sessionResponses.add(castToObjectModel(item));
        }
        return sessionResponses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public int getSession_duration() {
        return session_duration;
    }

    public void setSession_duration(int session_duration) {
        this.session_duration = session_duration;
    }

    public Session.AttendanceStatusEnum getAttendance_status() {
        return attendance_status;
    }

    public void setAttendance_status(Session.AttendanceStatusEnum attendance_status) {
        this.attendance_status = attendance_status;
    }

    public Session.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(Session.StatusEnum status) {
        this.status = status;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
