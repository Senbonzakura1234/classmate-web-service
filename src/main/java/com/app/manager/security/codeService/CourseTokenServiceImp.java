package com.app.manager.security.codeService;

import com.app.manager.context.repository.CourseRepository;
import com.app.manager.context.repository.UserRepository;
import com.app.manager.model.payload.response.CourseTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class CourseTokenServiceImp implements CourseTokenService {
    @Autowired CourseRepository courseRepository;
    @Autowired UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CourseTokenServiceImp.class);

    @Override
    public CourseTokenResponse generateCourseToken(String currentUsername, String courseId) {
        try {
            var course = courseRepository.findById(courseId);
            if(course.isEmpty())
                return new CourseTokenResponse("", courseId,
                        0L, false, HttpStatus.NOT_FOUND,
                        "course not found");

            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty())
                return new CourseTokenResponse("", courseId,
                        0L, false, HttpStatus.NOT_FOUND,
                        "user not found");

            if(!course.get().getUser_id().equals(currentUser.get().getId()))
                return new CourseTokenResponse("", courseId,
                        0L, false, HttpStatus.BAD_REQUEST,
                        "not your course");


            var c = course.get();
            c.setJoinable_by_token(true);
            c.setToken(UUID.randomUUID().toString()
                    .replace("-", "")
                    + System.currentTimeMillis()
                    + (System.currentTimeMillis() + 4L * 86400000L));
            c.setToken_expire_date(System.currentTimeMillis() + 4L * 86400000L);
            courseRepository.save(c);
            return new CourseTokenResponse(c.getToken(),
                    courseId, c.getToken_expire_date(), true,
                    HttpStatus.OK, "generate token success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new CourseTokenResponse("", courseId,
                    0L, false, HttpStatus.INTERNAL_SERVER_ERROR,
                    "error: " + e.getMessage());
        }
    }
}
