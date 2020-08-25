package com.app.manager.service.interfaceClass;

import com.app.manager.model.payload.request.StudentCourseRequest;
import com.app.manager.model.returnResult.DatabaseQueryResult;


public interface StudentCourseService {
    DatabaseQueryResult addStudentToCourse(StudentCourseRequest studentCourseRequest, String currentUsername);
}
