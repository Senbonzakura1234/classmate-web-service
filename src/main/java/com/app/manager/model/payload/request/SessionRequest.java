package com.app.manager.model.payload.request;

import com.app.manager.entity.Session;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class SessionRequest {
    @NotBlank
    private String courseId = "";
    @NotBlank
    private String name = "";
    @NotBlank
    private Long starttime = 0L;
    @NotBlank
    @Min(value = 1)
    private int sessionduration = 1;

    public SessionRequest() {
    }

    public SessionRequest(@NotBlank String courseId, @NotBlank String name,
                          @NotBlank Long starttime,
                          @NotBlank @Min(value = 0) int sessionduration) {
        this.courseId = courseId;
        this.name = name;
        this.starttime = starttime;
        this.sessionduration = sessionduration;
    }

    public static Session castToEntity(SessionRequest sessionRequest, String teacherId){
        Session session = new Session();

        session.setSessionduration(sessionRequest.getSessionduration());
        session.setName(sessionRequest.getName());
        session.setStarttime(sessionRequest.getStarttime());
        session.setUserid(teacherId);
        session.setCourseid(sessionRequest.getCourseId());
        return session;
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
}
