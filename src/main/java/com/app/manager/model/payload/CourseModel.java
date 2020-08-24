package com.app.manager.model.payload;

import com.app.manager.entity.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseModel {
    private String id;
    private String userid; //search
    private String coursecategoryid; //search
    private String name; //search
    private String description;
    private Long startdate; //search
    private Long enddate; //search
    private Long createdat;
    private Course.StatusEnum status; //search

    public CourseModel() {
    }

    public static List<CourseModel> castToListModels(List<Course> courses){
        List<CourseModel> courseModels = new ArrayList<>();
        if (courses == null) return courseModels;
        for (var item: courses) {
            courseModels.add(castToObjectModel(item));
        }
        return courseModels;
    }

    public CourseModel(String id, String userid, String coursecategoryid,
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

    public static Course castToEntity(CourseModel courseModel){
        Course course = new Course();
        course.setId(courseModel.getId());
        course.setCoursecategoryid(courseModel.getCoursecategoryid());
        course.setCreatedat(courseModel.getCreatedat());
        course.setDescription(courseModel.getDescription());
        course.setEnddate(courseModel.getEnddate());
        course.setName(courseModel.getName());
        course.setStartdate(courseModel.getStartdate());
        course.setUserid(courseModel.getUserid());
        course.setStatus(courseModel.getStatus());
        return course;
    }

    public static CourseModel castToObjectModel(Course course){
        CourseModel courseModel = new CourseModel();
        if(course == null){
            return courseModel;
        }
        courseModel.setId(course.getId());
        courseModel.setCoursecategoryid(course.getCoursecategoryid());
        courseModel.setCreatedat(course.getCreatedat());
        courseModel.setDescription(course.getDescription());
        courseModel.setEnddate(course.getEnddate());
        courseModel.setName(course.getName());
        courseModel.setStartdate(course.getStartdate());
        courseModel.setUserid(course.getUserid());
        courseModel.setStatus(course.getStatus());

        return courseModel;
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
