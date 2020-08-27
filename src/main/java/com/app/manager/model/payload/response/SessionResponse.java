package com.app.manager.model.payload.response;

import com.app.manager.entity.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionResponse {
    private String id;
    private String courseId; //search
    private String name; //search
    private Long starttime;
    private int sessionduration;
    private Session.AttendanceStatusEnum attendancestatus;
    private Session.StatusEnum status; //search
    private Long createdat;

    public SessionResponse(String id, String courseId,
                           String name, Long starttime, int sessionduration,
                           Session.AttendanceStatusEnum attendancestatus, Session.StatusEnum status,
                           Long createdat) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
        this.starttime = starttime;
        this.sessionduration = sessionduration;
        this.attendancestatus = attendancestatus;
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
        sessionResponse.setAttendancestatus(session.getAttendancestatus());
        sessionResponse.setSessionduration(session.getSessionduration());
        sessionResponse.setName(session.getName());
        sessionResponse.setStarttime(session.getStarttime());
        sessionResponse.setStatus(session.getStatus());
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

    public int getSessionduration() {
        return sessionduration;
    }

    public void setSessionduration(int sessionduration) {
        this.sessionduration = sessionduration;
    }

    public Session.AttendanceStatusEnum getAttendancestatus() {
        return attendancestatus;
    }

    public void setAttendancestatus(Session.AttendanceStatusEnum attendancestatus) {
        this.attendancestatus = attendancestatus;
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
