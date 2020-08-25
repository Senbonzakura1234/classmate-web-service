package com.app.manager.model.payload.request;

import com.app.manager.entity.Course;

import javax.validation.constraints.NotBlank;

public class CourseRequest {
    @NotBlank
    private String coursecategoryid = "";
    @NotBlank
    private String name = "";
    @NotBlank
    private String description = "";
    @NotBlank
    private Long startdate = 0L;
    @NotBlank
    private Long enddate = 0L;

    public CourseRequest() {
    }

    public CourseRequest(@NotBlank String coursecategoryid,
                         @NotBlank String name, @NotBlank String description,
                         @NotBlank Long startdate, @NotBlank Long enddate) {
        this.coursecategoryid = coursecategoryid;
        this.name = name;
        this.description = description;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public static Course castToEntity(CourseRequest courseRequest, String teacherId){
        Course course = new Course();
        course.setCoursecategoryid(courseRequest.getCoursecategoryid());
        course.setDescription(courseRequest.getDescription());
        course.setEnddate(courseRequest.getEnddate());
        course.setName(courseRequest.getName());
        course.setStartdate(courseRequest.getStartdate());
        course.setUserid(teacherId);
        return course;
    }



    public String getCoursecategoryid() {
        return coursecategoryid;
    }

    public void setCoursecategoryid(String coursecategoryid) {
        this.coursecategoryid = coursecategoryid;
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

    public Long getStartdate() {
        return startdate;
    }

    public void setStartdate(Long startdate) {
        this.startdate = startdate;
    }

    public Long getEnddate() {
        return enddate;
    }

    public void setEnddate(Long enddate) {
        this.enddate = enddate;
    }
}
