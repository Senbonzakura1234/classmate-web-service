package com.app.manager.model.payload;

import com.app.manager.entity.*;
import com.app.manager.model.payload.request.*;
import com.app.manager.model.payload.response.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Component
public class CastObject {
    public Course courseEntity(CourseRequest courseRequest, String teacherId){
        var course = new Course();
        course.setCoursecategory_id(courseRequest.getCourse_category_id());
        course.setDescription(courseRequest.getDescription());
        course.setCover_file_id(courseRequest.getCover_file_id());
        course.setEnd_date(courseRequest.getEnd_date());
        course.setName(courseRequest.getName());
        course.setStart_date(courseRequest.getStart_date());
        course.setUser_id(teacherId);
        course.setUpdated_at(System.currentTimeMillis());
        return course;
    }
    public CourseResponse courseModel(Course course, SessionResponse currentSession,
                              int studentCount, int sessionCount){
        if(course == null) return new CourseResponse();

        return new CourseResponse(course.getId(), course.getUser_id(),
                course.getCoursecategory_id(), course.getName(), course.getDescription(),
                course.getCover_file_id(),
                course.getStart_date(), course.getEnd_date(), course.getCreated_at(),
                studentCount, sessionCount, course.getStatus(), currentSession);
    }

    public Exercise exerciseEntity(ExerciseRequest exerciseRequest,
                                   @NotNull String course_id){
        var exercise = new Exercise();
        exercise.setSession_id(exerciseRequest.getSession_id());
        exercise.setTitle(exerciseRequest.getTitle());
        exercise.setContent(exerciseRequest.getContent());
        exercise.setAnswer(exerciseRequest.getAnswer());
        exercise.setExercise_end_time(exerciseRequest.getExercise_end_time());
        exercise.setShow_answer(exerciseRequest.isShow_answer());
        exercise.setAuto_start(exerciseRequest.isAuto_start());
        exercise.setAuto_close(exerciseRequest.isAuto_close());
        exercise.setCourse_id(course_id);
        exercise.setUpdated_at(System.currentTimeMillis());
        return exercise;
    }
    public ExerciseResponse exerciseModel(Exercise exercise,
                              int submitedCount, int markCount){
        if(exercise == null) return new ExerciseResponse();
        return new ExerciseResponse(exercise.getId(), exercise.getSession_id(),
                exercise.getTitle(), exercise.getContent(),
                exercise.isShow_answer() ? exercise.getAnswer() : "",
                exercise.getExercise_end_time(), exercise.isShow_answer(),
                exercise.isAuto_start(), exercise.isAuto_close(),
                exercise.getStatus(), exercise.getCreated_at(),
                new ArrayList<>(), submitedCount, markCount);
    }
    public ExerciseResponse exerciseModelTeacher(Exercise exercise,
            List<GradeRecordResponse> gradeRecordResponses,
            int submitedCount, int markCount){
        if(exercise == null) return new ExerciseResponse();
        return new ExerciseResponse(exercise.getId(), exercise.getSession_id(),
                exercise.getTitle(), exercise.getContent(),
                exercise.getAnswer(), exercise.getExercise_end_time(),
                exercise.isShow_answer(), exercise.isAuto_start(),
                exercise.isAuto_close(), exercise.getStatus(),
                exercise.getCreated_at(), gradeRecordResponses,
                submitedCount, markCount);
    }
    public ExerciseResponse exerciseModelStudent(Exercise exercise,
            List<GradeRecordResponse> gradeRecordResponses){
        if(exercise == null) return new ExerciseResponse();
        return new ExerciseResponse(exercise.getId(), exercise.getSession_id(),
                exercise.getTitle(), exercise.getContent(),
                exercise.isShow_answer()? exercise.getAnswer() : "",
                exercise.getExercise_end_time(), exercise.isShow_answer(),
                exercise.isAuto_start(), exercise.isAuto_close(),
                exercise.getStatus(), exercise.getCreated_at(),
                gradeRecordResponses, 0, 0);
    }
    public ExerciseResponse exerciseModelPublic(Exercise exercise){
        if(exercise == null) return new ExerciseResponse();
        return new ExerciseResponse(exercise.getId(), exercise.getSession_id(),
                exercise.getTitle(), "", "",
                exercise.getExercise_end_time(), exercise.isShow_answer(),
                exercise.isAuto_start(), exercise.isAuto_close(),
                exercise.getStatus(), exercise.getCreated_at(),
                new ArrayList<>(), 0, 0);
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
                user.getPhone(), user.getAvatar_file_id(),
                user.getAddress(), user.getCivil_id(),
                user.getBirthday(), user.getGender());
    }
    public UserProfileResponse profilePrivate(User user){
        if(user == null) return new UserProfileResponse();
        return new UserProfileResponse(user.getId(), user.getUsername(),
                user.getAvatar_file_id());
    }

    public StudentExercise studentExerciseEntity(String studentId, String exerciseId,
            StudentExerciseRequest studentExerciseRequest){
        var studentExercise = new StudentExercise();
        studentExercise.setUser_id(studentId);
        studentExercise.setSubmitted(true);
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
        file.setFile_id(fileRequest.getFile_id());
        file.setFile_size(fileRequest.getFile_size());
        file.setUpdated_at(System.currentTimeMillis());
        return file;
    }
    public FileResponse fileModel(File file){
        return file != null ? new FileResponse(
                file.getId(), file.getStudentexercise_id(), file.getName(),
                file.getDescription(), file.getFile_id(), file.getFile_size(),
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
                fileResponses, studentExercise.isSubmitted()) : new StudentExerciseResponse();
    }

    public StudentExerciseResponse studentExerciseModelPublic(
            StudentExercise studentExercise){
        return new StudentExerciseResponse(studentExercise.getId(), studentExercise.getUser_id(),
                studentExercise.getExercise_id(), studentExercise.getStatus(),
                studentExercise.getCreated_at(), studentExercise.isMarked(),
                studentExercise.isSubmitted());
    }

    public StudentExerciseResponse studentExerciseModelGradeList(
            StudentExercise studentExercise){
        return new StudentExerciseResponse(studentExercise.getId(),
                studentExercise.getUser_id(), studentExercise.getExercise_id(),
                studentExercise.getStatus(), studentExercise.getCreated_at(),
                studentExercise.getMark(), studentExercise.isMarked(),
                studentExercise.isSubmitted());
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
        attachment.setFile_id(attachmentRequest.getFile_id());
        attachment.setFile_size(attachmentRequest.getFile_size());
        attachment.setUpdated_at(System.currentTimeMillis());
        return attachment;

    }
    public AttachmentResponse attachmentModel(Attachment attachment){
        return attachment != null ?
            new AttachmentResponse(attachment.getId(), attachment.getPost_id(),
            attachment.getName(), attachment.getDescription(), attachment.getFile_id(),
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
                                  Post post, List<AttachmentResponse> attachmentResponses,
                                  List<CommentResponse> commentResponses){
        return post != null ? new PostResponse(post.getId(), post.getContent(),
                post.isPin(), post.getUser_id(), userProfileResponse,
                attachmentResponses, commentResponses,
                post.getCourse_id(), post.getStatus(),
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
        return comment != null ? new CommentResponse(comment.getId(),
                comment.getUser_id(), userProfileResponse, comment.getPost_id(),
                comment.getContent(), comment.isPin(), comment.getStatus(),
                comment.getCreated_at()) : new CommentResponse();
    }
}
