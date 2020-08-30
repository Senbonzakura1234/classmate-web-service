package com.app.manager.model.payload.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SessionRequest {
    @NotBlank
    private String course_id = "";
    @NotBlank
    private String name = "";
    private String content = "";
    @NotNull
    private Long start_time = 0L;
    @NotNull
    @Min(value = 1)
    private int session_duration = 1;

    public SessionRequest() {
    }

    public SessionRequest(@NotBlank String course_id, @NotBlank String name, String content,
                          @NotNull Long start_time, @NotNull @Min(value = 1) int session_duration) {
        this.course_id = course_id;
        this.name = name;
        this.content = content;
        this.start_time = start_time;
        this.session_duration = session_duration;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
