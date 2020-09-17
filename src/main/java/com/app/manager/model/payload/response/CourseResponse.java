package com.app.manager.model.payload.response;

import com.app.manager.entity.Course;

public class CourseResponse {
    private String id;
    private String user_id; //search
    private String course_category_id; //search
    private String name; //search
    private String description;
    private Long start_date; //search
    private Long end_date; //search
    private Long created_at;
    private int studentCount = 0;
    private int sessionCount = 0;
    private Course.StatusEnum status; //search
    private SessionResponse currentSession;

    public CourseResponse() {
    }


    public CourseResponse(String id, String user_id, String course_category_id,
                          String name, String description, Long start_date, Long end_date,
                          Long created_at, int studentCount, int sessionCount,
                          Course.StatusEnum status, SessionResponse currentSession) {
        this.id = id;
        this.user_id = user_id;
        this.course_category_id = course_category_id;
        this.name = name;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.created_at = created_at;
        this.studentCount = studentCount;
        this.sessionCount = sessionCount;
        this.status = status;
        this.currentSession = currentSession;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Course.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(Course.StatusEnum status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCourse_category_id() {
        return course_category_id;
    }

    public void setCourse_category_id(String course_category_id) {
        this.course_category_id = course_category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStart_date() {
        return start_date;
    }

    public void setStart_date(Long start_date) {
        this.start_date = start_date;
    }

    public Long getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Long end_date) {
        this.end_date = end_date;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public SessionResponse getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(SessionResponse currentSession) {
        this.currentSession = currentSession;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }
}
