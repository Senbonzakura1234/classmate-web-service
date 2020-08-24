package com.app.manager.model.payload;

import com.app.manager.entity.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionModel {
    private String id;
    private String courseId; //search
    private String userId; //search
    private String name; //search
    private Long starttime;
    private int attendanceduration;
    private boolean attendancechecked;
    private Session.StatusEnum status; //search
    private Long createdat;

    public SessionModel(String id, String courseId, String userId,
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

    public SessionModel() {
    }

    public static SessionModel castToObjectModel(Session session){
        SessionModel sessionModel = new SessionModel();
        if(session == null){
            return sessionModel;
        }
        sessionModel.setId(session.getId());
        sessionModel.setCreatedat(session.getCreatedat());
        sessionModel.setAttendancechecked(session.isAttendancechecked());
        sessionModel.setAttendanceduration(session.getAttendanceduration());
        sessionModel.setName(session.getName());
        sessionModel.setStarttime(session.getStarttime());
        sessionModel.setStatus(session.getStatus());
        sessionModel.setUserId(session.getUserid());
        sessionModel.setCourseId(session.getCourseid());

        return sessionModel;
    }

    public static Session castToEntity(SessionModel sessionModel){
        Session session = new Session();
        session.setId(sessionModel.getId());
        session.setCreatedat(sessionModel.getCreatedat());
        session.setAttendancechecked(sessionModel.isAttendancechecked());
        session.setAttendanceduration(sessionModel.getAttendanceduration());
        session.setName(sessionModel.getName());
        session.setStarttime(sessionModel.getStarttime());
        session.setStatus(sessionModel.getStatus());
        session.setUserid(sessionModel.getUserId());
        session.setCourseid(sessionModel.getCourseId());
        return session;
    }

    public static List<SessionModel> castToListModels(List<Session> sessions){
        List<SessionModel> sessionModels = new ArrayList<>();
        if (sessions == null) return sessionModels;
        for (var item: sessions) {
            sessionModels.add(castToObjectModel(item));
        }
        return sessionModels;
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
