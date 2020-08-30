package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CourseRequest {
    @NotBlank
    private String course_category_id = "";
    @NotBlank
    private String name = "";
    @NotBlank
    private String description = "";
    @NotNull
    private Long start_date = 0L;
    @NotNull
    private Long end_date = 0L;

    public CourseRequest() {
    }

    public CourseRequest(@NotBlank String course_category_id, @NotBlank String name,
                         @NotBlank String description, @NotNull Long start_date,
                         @NotNull Long end_date) {
        this.course_category_id = course_category_id;
        this.name = name;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
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
}
