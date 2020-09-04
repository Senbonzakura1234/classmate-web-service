package com.app.manager.model.payload.response;

import java.util.List;

public class CourseProfileResponse {
    private CourseResponse courseResponse;
    private UserProfileResponse teacher;
    private List<UserProfileResponse> students;

    public CourseProfileResponse() {
    }

    public CourseProfileResponse(CourseResponse courseResponse,
                                 UserProfileResponse teacher,
                                 List<UserProfileResponse> students) {
        this.courseResponse = courseResponse;
        this.teacher = teacher;
        this.students = students;
    }

    public CourseResponse getCourseResponse() {
        return courseResponse;
    }

    public void setCourseResponse(CourseResponse courseResponse) {
        this.courseResponse = courseResponse;
    }

    public UserProfileResponse getTeacher() {
        return teacher;
    }

    public void setTeacher(UserProfileResponse teacher) {
        this.teacher = teacher;
    }

    public List<UserProfileResponse> getStudents() {
        return students;
    }

    public void setStudents(List<UserProfileResponse> students) {
        this.students = students;
    }
}
