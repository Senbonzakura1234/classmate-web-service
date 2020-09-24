package com.app.manager.service.interfaceClass;

import com.app.manager.model.payload.request.JoinCourseByTokenRequest;
import com.app.manager.model.payload.request.StudentCourseRequest;
import com.app.manager.model.payload.response.CourseProfileResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.Optional;


public interface StudentCourseService {
    DatabaseQueryResult addStudentToCourse(StudentCourseRequest studentCourseRequest,
                                           String currentUsername);
    DatabaseQueryResult addStudentToCourseByToken(JoinCourseByTokenRequest joinCourseByTokenRequest,
                                                  String currentUsername);
    DatabaseQueryResult removeStudentFromCourse(StudentCourseRequest studentCourseRequest,
                                           String currentUsername);
    Optional<CourseProfileResponse> getAllProfileInCourse(String currentUsername,
                                                          String courseId);


}
