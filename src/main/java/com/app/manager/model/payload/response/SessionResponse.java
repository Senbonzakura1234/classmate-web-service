package com.app.manager.model.payload.response;

import com.app.manager.entity.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionResponse {
    private String id;
    private String courseId; //search
    private String userId; //search
    private String name; //search
    private Long starttime;
    private int attendanceduration;
    private boolean attendancechecked;
    private Session.StatusEnum status; //search
    private Long createdat;

    public SessionResponse(String id, String courseId, String userId,
                           String name, Long starttime, int attendanceduration,
                           boolean attendancechecked, Session.StatusEnum status,
                           Long createdat) {
        this.id = id;
        this.courseId = courseId;
        this.userId = userId;
        this.name = name;
        this.starttime = starttime;
        this.attendanceduration = attendanceduration;
        this.attendancechecked = attendancechecked;
        this.status = status;
        this.createdat = createdat;
    }

    public SessionResponse() {
    }

    public static SessionResponse castToObjectModel(Session session){
        SessionResponse sessionResponse = new SessionResponse();
        if(session == null){
            return sessionResponse;
        }
        sessionResponse.setId(session.getId());
        sessionResponse.setCreatedat(session.getCreatedat());
        sessionResponse.setAttendancechecked(session.isAttendancechecked());
        sessionResponse.setAttendanceduration(session.getAttendanceduration());
        sessionResponse.setName(session.getName());
        sessionResponse.setStarttime(session.getStarttime());
        sessionResponse.setStatus(session.getStatus());
        sessionResponse.setUserId(session.getUserid());
        sessionResponse.setCourseId(session.getCourseid());

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

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStarttime() {
        return starttime;
    }

    public void setStarttime(Long starttime) {
        this.starttime = starttime;
    }

    public int getAttendanceduration() {
        return attendanceduration;
    }

    public void setAttendanceduration(int attendanceduration) {
        this.attendanceduration = attendanceduration;
    }

    public boolean isAttendancechecked() {
        return attendancechecked;
    }

    public void setAttendancechecked(boolean attendancechecked) {
        this.attendancechecked = attendancechecked;
    }

    public Session.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(Session.StatusEnum status) {
        this.status = status;
    }

    public Long getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Long createdat) {
        this.createdat = createdat;
    }
}
