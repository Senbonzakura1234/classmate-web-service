package com.app.manager.security.codeService;

import com.app.manager.model.payload.response.CourseTokenResponse;

public interface CourseTokenService {
    CourseTokenResponse generateCourseToken(String currentUsername, String courseId);
}
