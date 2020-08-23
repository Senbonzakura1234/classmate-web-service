package com.app.manager.model.midware_model;

import com.app.manager.entity.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseModel {
    private String coursecategoryid; //update
    private String name; //update
    private String description; //update
    private Long startdate; //update
    private Long enddate; //update
    private Long createdat; //update

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

    public CourseModel(String coursecategoryid, String name, String description, Long startdate, Long enddate, Long createdat) {
        this.coursecategoryid = coursecategoryid;
        this.name = name;
        this.description = description;
        this.startdate = startdate;
        this.enddate = enddate;
        this.createdat = createdat;
    }

    public static Course castToEntity(CourseModel courseModel){
        Course course = new Course();
        course.setCoursecategoryid(courseModel.getCoursecategoryid());
        course.setCreatedat(courseModel.getCreatedat());
        course.setDescription(courseModel.getDescription());
        course.setEnddate(courseModel.getEnddate());
        course.setName(courseModel.getName());
        course.setStartdate(courseModel.getStartdate());
        return course;
    }

    public static CourseModel castToObjectModel(Course course){
        CourseModel courseModel = new CourseModel();
        if(course == null){
            return courseModel;
        }
        courseModel.setCoursecategoryid(course.getCoursecategoryid());
        courseModel.setCreatedat(course.getCreatedat());
        courseModel.setDescription(course.getDescription());
        courseModel.setEnddate(course.getEnddate());
        courseModel.setName(course.getName());
        courseModel.setStartdate(course.getStartdate());

        return courseModel;
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
