package com.app.manager.model.payload;

import com.app.manager.entity.*;
import com.app.manager.model.payload.request.*;
import com.app.manager.model.payload.response.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CastObject {
    public Course courseEntity(CourseRequest courseRequest, String teacherId){
        var course = new Course();
        course.setCoursecategory_id(courseRequest.getCourse_category_id());
        course.setDescription(courseRequest.getDescription());
        course.setEnd_date(courseRequest.getEnd_date());
        course.setName(courseRequest.getName());
        course.setStart_date(courseRequest.getStart_date());
        course.setUser_id(teacherId);
        course.setUpdated_at(System.currentTimeMillis());
        return course;
    }
    public CourseResponse courseModel(Course course){
        if(course == null) return new CourseResponse();

        return new CourseResponse(course.getId(), course.getUser_id(),
                course.getCoursecategory_id(), course.getName(), course.getDescription(),
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
        exercise.setUpdated_at(System.currentTimeMillis());
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
    public ExerciseResponse exerciseModelPublic(Exercise exercise){
        if(exercise == null) return new ExerciseResponse();
        return new ExerciseResponse(exercise.getId(), exercise.getSession_id(),
                exercise.getTitle(), "", "",
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
        session.setUpdated_at(System.currentTimeMillis());
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
    public SessionResponse sessionModelPublic(Session session){
        if(session == null) return new SessionResponse();
        return new SessionResponse(
                session.getId(), session.getCourse_id(), session.getName(),
                "", session.getStart_time(),
                session.getSession_duration(), session.getAttendance_status(),
                session.getStatus(), session.getCreated_at());
    }


    public UserProfileResponse profilePublic(User user){
        if(user == null) return new UserProfileResponse();
        return new UserProfileResponse(
                user.getId(), user.getUsername(),
                user.getEmail(), user.getFullname(),
                user.getPhone(), user.getAvatar_uri(),
                user.getAddress(), user.getCivil_id(),
                user.getBirthday(), user.getGender());
    }
    public UserProfileResponse profilePrivate(User user){
        if(user == null) return new UserProfileResponse();
        return new UserProfileResponse(user.getId(), user.getUsername(),
                user.getAvatar_uri());
    }

    public StudentExercise studentExerciseEntity(String studentId, String exerciseId,
            StudentExerciseRequest studentExerciseRequest){
        var studentExercise = new StudentExercise();
        studentExercise.setUser_id(studentId);
        studentExercise.setExercise_id(exerciseId);
        studentExercise.setContent(studentExerciseRequest.getContent());
        studentExercise.setStudent_message(studentExerciseRequest.getStudent_message());
        studentExercise.setUpdated_at(System.currentTimeMillis());
        return studentExercise;
    }

    public File fileEntity(String studentExerciseId, FileRequest fileRequest){
        var file = new File();
        file.setStudentexercise_id(studentExerciseId);
        file.setName(fileRequest.getName());
        file.setDescription(fileRequest.getDescription());
        file.setFile_url(fileRequest.getFile_url());
        file.setFile_size(fileRequest.getFile_size());
        file.setUpdated_at(System.currentTimeMillis());
        return file;
    }
    public FileResponse fileModel(File file){
        return file != null ? new FileResponse(
                file.getId(), file.getStudentexercise_id(), file.getName(),
                file.getDescription(), file.getFile_url(), file.getFile_size(),
                file.getFile_visibility(), file.getStatus(), file.getCreated_at())
                : new FileResponse();
    }

    public StudentExerciseResponse studentExerciseModel(
            StudentExercise studentExercise, List<FileResponse> fileResponses){
        return studentExercise != null && fileResponses != null ? new StudentExerciseResponse(
            studentExercise.getId(), studentExercise.getUser_id(),
            studentExercise.getExercise_id(), studentExercise.getContent(),
            studentExercise.getStudent_message(), studentExercise.getStatus(),
            studentExercise.getCreated_at(), studentExercise.getMark(),
            studentExercise.isMarked(), studentExercise.getTeacher_message(),
                fileResponses) : new StudentExerciseResponse();
    }

    public StudentExerciseResponse studentExerciseModelPublic(
            StudentExercise studentExercise){
        return new StudentExerciseResponse(studentExercise.getId(), studentExercise.getUser_id(),
                studentExercise.getExercise_id(), "", "", studentExercise.getStatus(),
                studentExercise.getCreated_at(), new ArrayList<>());
    }

    public CourseCategory courseCategoryEntity(
            CourseCategoryRequest courseCategoryRequest){
        var courseCategory = new CourseCategory();
        courseCategory.setName(courseCategoryRequest.getName());
        courseCategory.setDescription(courseCategoryRequest.getDescription());
        courseCategory.setUpdated_at(System.currentTimeMillis());
        return courseCategory;
    }

    public CourseCategoryResponse courseCategoryModel(CourseCategory courseCategory){
        return courseCategory != null ? new CourseCategoryResponse(
                courseCategory.getId(), courseCategory.getName(),
                courseCategory.getDescription()) : new CourseCategoryResponse();
    }


    public Attachment attachmentEntity(String postId,
                                       AttachmentRequest attachmentRequest){
        var attachment = new Attachment();
        attachment.setPost_id(postId);
        attachment.setName(attachmentRequest.getName());
        attachment.setDescription(attachmentRequest.getDescription());
        attachment.setFile_url(attachmentRequest.getFile_url());
        attachment.setFile_size(attachmentRequest.getFile_size());
        attachment.setUpdated_at(System.currentTimeMillis());
        return attachment;

    }
    public AttachmentResponse attachmentModel(Attachment attachment){
        return attachment != null ?
            new AttachmentResponse(attachment.getId(), attachment.getPost_id(),
            attachment.getName(), attachment.getDescription(), attachment.getFile_url(),
            attachment.getFile_size(), attachment.getStatus(), attachment.getCreated_at())
            : new AttachmentResponse();
    }

    public Post postEntity(String userId, String courseId,
                           PostRequest postRequest){
        var post = new Post();
        post.setUser_id(userId);
        post.setCourse_id(courseId);
        post.setContent(postRequest.getContent());
        post.setUpdated_at(System.currentTimeMillis());
        return post;
    }
    public PostResponse postModel(UserProfileResponse userProfileResponse,
                                  Post post, List<AttachmentResponse> attachmentResponses){
        return post != null ? new PostResponse(post.getId(), post.getContent(),
                post.isPin(), post.getUser_id(), userProfileResponse,
                attachmentResponses, post.getCourse_id(), post.getStatus(),
                post.getCreated_at()) : new PostResponse();
    }

    public Comment commentEntity(String userId, String postId,
                                 CommentRequest commentRequest){
        var comment = new Comment();
        comment.setUser_id(userId);
        comment.setPost_id(postId);
        comment.setContent(commentRequest.getContent());
        comment.setUpdated_at(System.currentTimeMillis());
        return comment;
    }
    public CommentResponse commentModel(UserProfileResponse userProfileResponse,
                                        Comment comment){
        return comment != null ? new CommentResponse(comment.getUser_id(),
                userProfileResponse, comment.getPost_id(), comment.getContent(),
                comment.isPin(), comment.getStatus(), comment.getCreated_at()) :
                new CommentResponse();
    }
}
