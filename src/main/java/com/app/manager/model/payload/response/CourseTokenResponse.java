package com.app.manager.model.payload.response;

import org.springframework.http.HttpStatus;

public class CourseTokenResponse {
    private String token;
    private String course_id;
    private Long expire_time;
    private boolean success;
    private HttpStatus http_status;
    private String message;

    public CourseTokenResponse() {
    }

    public CourseTokenResponse(String token, String course_id, Long expire_time,
                               boolean success, HttpStatus http_status, String message) {
        this.token = token;
        this.course_id = course_id;
        this.expire_time = expire_time;
        this.success = success;
        this.http_status = http_status;
        this.message = message;
    }

    public HttpStatus getHttp_status() {
        return http_status;
    }

    public void setHttp_status(HttpStatus http_status) {
        this.http_status = http_status;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(Long expire_time) {
        this.expire_time = expire_time;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
