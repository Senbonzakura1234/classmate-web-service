package com.app.manager.service.interfaceClass;

import com.app.manager.model.payload.request.StudentCourseRequest;
import com.app.manager.model.payload.response.CourseProfileResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.Optional;


public interface StudentCourseService {
    DatabaseQueryResult addStudentToCourse(StudentCourseRequest studentCourseRequest,
                                           String currentUsername);
    DatabaseQueryResult removeStudentFromCourse(StudentCourseRequest studentCourseRequest,
                                           String currentUsername);
    Optional<CourseProfileResponse> getAllProfileInCourse(String courseId);


}
