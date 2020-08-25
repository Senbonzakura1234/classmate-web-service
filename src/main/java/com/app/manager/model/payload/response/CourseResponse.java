package com.app.manager.model.payload.response;

import com.app.manager.entity.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseResponse {
    private String id;
    private String userid; //search
    private String coursecategoryid; //search
    private String name; //search
    private String description;
    private Long startdate; //search
    private Long enddate; //search
    private Long createdat;
    private Course.StatusEnum status; //search

    public CourseResponse() {
    }

    public static List<CourseResponse> castToListModels(List<Course> courses){
        List<CourseResponse> courseResponses = new ArrayList<>();
        if (courses == null) return courseResponses;
        for (var item: courses) {
            courseResponses.add(castToObjectModel(item));
        }
        return courseResponses;
    }

    public CourseResponse(String id, String userid, String coursecategoryid,
                          String name, String description, Long startdate,
                          Long enddate, Long createdat, Course.StatusEnum status) {
        this.id = id;
        this.userid = userid;
        this.coursecategoryid = coursecategoryid;
        this.name = name;
        this.description = description;
        this.startdate = startdate;
        this.enddate = enddate;
        this.createdat = createdat;
        this.status = status;
    }

    public static CourseResponse castToObjectModel(Course course){
        CourseResponse courseResponse = new CourseResponse();
        if(course == null){
            return courseResponse;
        }
        courseResponse.setId(course.getId());
        courseResponse.setCoursecategoryid(course.getCoursecategoryid());
        courseResponse.setCreatedat(course.getCreatedat());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setEnddate(course.getEnddate());
        courseResponse.setName(course.getName());
        courseResponse.setStartdate(course.getStartdate());
        courseResponse.setUserid(course.getUserid());
        courseResponse.setStatus(course.getStatus());

        return courseResponse;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public Long getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Long createdat) {
        this.createdat = createdat;
    }


}
