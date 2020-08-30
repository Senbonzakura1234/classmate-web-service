package com.app.manager.model.payload;

import com.app.manager.entity.Course;
import com.app.manager.entity.Exercise;
import com.app.manager.entity.Session;
import com.app.manager.entity.User;
import com.app.manager.model.payload.request.CourseRequest;
import com.app.manager.model.payload.request.ExerciseRequest;
import com.app.manager.model.payload.request.SessionRequest;
import com.app.manager.model.payload.response.CourseResponse;
import com.app.manager.model.payload.response.ExerciseResponse;
import com.app.manager.model.payload.response.SessionResponse;
import com.app.manager.model.payload.response.UserProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class CastObject {
    public Course courseEntity(CourseRequest courseRequest, String teacherId){
        var course = new Course();
        course.setCourse_category_id(courseRequest.getCourse_category_id());
        course.setDescription(courseRequest.getDescription());
        course.setEnd_date(courseRequest.getEnd_date());
        course.setName(courseRequest.getName());
        course.setStart_date(courseRequest.getStart_date());
        course.setUser_id(teacherId);
        return course;
    }
    public CourseResponse courseModel(Course course){
        if(course == null) return new CourseResponse();

        return new CourseResponse(course.getId(), course.getUser_id(),
                course.getCourse_category_id(), course.getName(), course.getDescription(),
                course.getStart_date(), course.getEnd_date(), course.getCreated_at(),
                course.getStatus());
    }

    public Exercise exerciseEntity(ExerciseRequest exerciseRequest){
        var exercise = new Exercise();
        exercise.setSession_id(exerciseRequest.getSession_id());
        exercise.setTitle(exerciseRequest.getTitle());
        exercise.setContent(exerciseRequest.getContent());
        exercise.setAnswer(exerciseRequest.getAnswer());
        exercise.setDuration(exerciseRequest.getDuration());
        exercise.setShow_answer(exerciseRequest.isShow_answer());
        return exercise;
    }
    public ExerciseResponse exerciseModel(Exercise exercise){
        if(exercise == null) return new ExerciseResponse();
        return new ExerciseResponse(exercise.getId(), exercise.getSession_id(),
                exercise.getTitle(), exercise.getContent(),
                exercise.isShow_answer() ? exercise.getAnswer() : "",
                exercise.getDuration(), exercise.isShow_answer(),
                exercise.getStatus(), exercise.getCreated_at());
    }

    public Session sessionEntity(SessionRequest sessionRequest){
        var session = new Session();

        session.setSession_duration(sessionRequest.getSession_duration());
        session.setName(sessionRequest.getName());
        session.setContent(sessionRequest.getContent());
        session.setStart_time(sessionRequest.getStart_time());
        session.setCourse_id(sessionRequest.getCourse_id());
        return session;
    }
    public SessionResponse sessionModel(Session session){
        if(session == null) return new SessionResponse();
        return new SessionResponse(
                session.getId(), session.getCourse_id(), session.getName(),
                session.getContent(), session.getStart_time(),
                session.getSession_duration(), session.getAttendance_status(),
                session.getStatus(), session.getCreated_at());
    }


    public UserProfileResponse profilePublic(User user){
        if(user == null) return new UserProfileResponse();
        return new UserProfileResponse(
                user.getId(), user.getUsername(),
                user.getEmail(), user.getFullname(),
                user.getPhone(), user.getAddress(),
                user.getCivil_id(), user.getBirthday(),
                user.getGender());
    }
    public UserProfileResponse profilePrivate(User user){
        if(user == null) return new UserProfileResponse();
        return new UserProfileResponse(user.getId(), user.getUsername());
    }
}
