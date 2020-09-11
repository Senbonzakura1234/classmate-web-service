package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.entity.Comment;
import com.app.manager.entity.ERole;
import com.app.manager.entity.Post;
import com.app.manager.entity.StudentCourse;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.CommentRequest;
import com.app.manager.model.payload.response.CommentResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class CommentServiceImp implements CommentService {
    @Autowired CommentRepository commentRepository;
    @Autowired PostRepository postRepository;
    @Autowired AttachmentRepository attachmentRepository;
    @Autowired UserRepository userRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired StudentCourseRepository studentCourseRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired CastObject castObject;

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImp.class);

    @Override
    public List<CommentResponse> getAllByPost(String postId, String currentUsername) {
        var currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        var post = postRepository.findFirstByIdAndStatus(postId, Post.StatusEnum.SHOW)
                .orElseThrow(() -> new RuntimeException("post not found"));
        var course = courseRepository.findById(post.getCourse_id())
                .orElseThrow(() -> new RuntimeException("course not found"));
        var role = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("role not found"));

        if(!currentUser.getRoles().contains(role) &&
            !course.getUser_id().equals(currentUser.getId()) &&
            studentCourseRepository.findAllByCourse_idAndStatus
            (course.getId(), StudentCourse.StatusEnum.SHOW)
            .stream().noneMatch(studentCourse -> studentCourse.getUser_id()
            .equals(currentUser.getId()))) return new ArrayList<>();


        return commentRepository.findAllByPost_idAndStatus
            (postId, Comment.StatusEnum.SHOW).stream().map(comment -> {
                try {
                    var user = userRepository.findById(comment.getUser_id())
                        .orElseThrow(() -> new RuntimeException("user not found"));
                    var profile = castObject.profilePrivate(user);
                    return castObject.commentModel(profile, comment);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return new CommentResponse();
                }
        }).filter(commentResponse -> commentResponse.getId() != null)
            .collect(Collectors.toList());
    }

    @Override
    public DatabaseQueryResult save(CommentRequest commentRequest,
                                    String currentUsername, String postId) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            var post = postRepository.findFirstByIdAndStatus(postId, Post.StatusEnum.SHOW)
                    .orElseThrow(() -> new RuntimeException("post not found"));
            var course = courseRepository.findById(post.getCourse_id())
                    .orElseThrow(() -> new RuntimeException("course not found"));
            var role = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("role not found"));

            if (!currentUser.getRoles().contains(role) &&
                !course.getUser_id().equals(currentUser.getId()) &&
                studentCourseRepository.findAllByCourse_idAndStatus
                (course.getId(), StudentCourse.StatusEnum.SHOW)
                .stream().noneMatch(studentCourse -> studentCourse.getUser_id()
                .equals(currentUser.getId())))
                return new DatabaseQueryResult(false,
                        "not your course",
                        HttpStatus.BAD_REQUEST, commentRequest);

            commentRepository.save(castObject
                    .commentEntity(currentUser.getId(), postId, commentRequest));

            return new DatabaseQueryResult(true,
                    "post comment success",
                    HttpStatus.OK, commentRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(false,
                    "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    commentRequest);
        }
    }

    @Override
    public DatabaseQueryResult edit(CommentRequest commentRequest,
                                    String commentId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty())
                return new DatabaseQueryResult(false,
                        "user not found",
                        HttpStatus.NOT_FOUND, commentRequest);

            var comment = commentRepository
                    .findFirstByIdAndStatus(commentId, Comment.StatusEnum.SHOW);
            if(comment.isEmpty())
                return new DatabaseQueryResult(false,
                        "comment not found",
                        HttpStatus.NOT_FOUND, commentRequest);

            if(!comment.get().getUser_id().equals(currentUser.get().getId()))
                return new DatabaseQueryResult(false,
                        "not your comment",
                        HttpStatus.BAD_REQUEST, commentRequest);

            var c = comment.get();
            c.setContent(commentRequest.getContent());
            c.setUpdated_at(System.currentTimeMillis());
            commentRepository.save(c);
            return new DatabaseQueryResult(true,
                    "update comment success",
                    HttpStatus.OK, commentRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(false,
                    "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    commentRequest);
        }
    }

    @Override
    public DatabaseQueryResult updatePin(String commentId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty())
                return new DatabaseQueryResult(false,
                        "user not found",
                        HttpStatus.NOT_FOUND, "");

            var comment = commentRepository
                    .findFirstByIdAndStatus(commentId, Comment.StatusEnum.SHOW);
            if(comment.isEmpty())
                return new DatabaseQueryResult(false,
                        "comment not found",
                        HttpStatus.NOT_FOUND, "");

            var post = postRepository
                    .findFirstByIdAndStatus(comment.get().getPost_id(),
                            Post.StatusEnum.SHOW);
            if(post.isEmpty()) return new DatabaseQueryResult(
                    false, "post not found",
                    HttpStatus.NOT_FOUND, "");

            var course = courseRepository
                    .findById(post.get().getCourse_id());
            if(course.isEmpty()) return new DatabaseQueryResult(
                    false, "course not found",
                    HttpStatus.NOT_FOUND, "");

            if(!course.get().getUser_id().equals(currentUser.get().getId()))
                return new DatabaseQueryResult(
                        false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");

            var c = comment.get();
            var isPin = c.isPin();
            if(!isPin) removeOldPin(c.getPost_id());
            c.setPin(!isPin);
            c.setUpdated_at(System.currentTimeMillis());
            commentRepository.save(c);

            return new DatabaseQueryResult(
                    true, "Pin comment success",
                    HttpStatus.OK, "");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(false,
                    "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "");
        }
    }

    @Override
    public DatabaseQueryResult delete(String commentId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty()) return new DatabaseQueryResult(
                    false, "User not found",
                    HttpStatus.NOT_FOUND, "");
            var comment = commentRepository
                    .findFirstByIdAndStatus(commentId, Comment.StatusEnum.SHOW);
            if(comment.isEmpty()) return new DatabaseQueryResult(
                    false, "comment not found",
                    HttpStatus.NOT_FOUND, "");

            var post = postRepository
                    .findFirstByIdAndStatus(comment.get().getPost_id(), Post.StatusEnum.SHOW);
            if(post.isEmpty()) return new DatabaseQueryResult(
                    false, "post not found",
                    HttpStatus.NOT_FOUND, "");

            var course = courseRepository
                    .findById(post.get().getCourse_id());
            if(course.isEmpty()) return new DatabaseQueryResult(
                    false, "course not found",
                    HttpStatus.NOT_FOUND, "");

            if(!currentUser.get().getId().equals(comment.get().getUser_id())
                && !currentUser.get().getId().equals(post.get().getUser_id())
                && !currentUser.get().getId().equals(course.get().getUser_id()))
                return new DatabaseQueryResult(false,
                    "you have no authority to delete this comment",
                    HttpStatus.BAD_REQUEST, "");

            var c = comment.get();
            c.setUpdated_at(System.currentTimeMillis());
            c.setDeleted_at(System.currentTimeMillis());
            c.setStatus(Comment.StatusEnum.HIDE);
            commentRepository.save(c);

            return new DatabaseQueryResult(
                    true, "delete comment success",
                    HttpStatus.BAD_REQUEST, "");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(
                    false, "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public DatabaseQueryResult updateStatus(String commentId, Comment.StatusEnum status) {
        try {
            var comment = commentRepository.findById(commentId);
            if(comment.isEmpty()) return new DatabaseQueryResult(false,
                    "comment not found", HttpStatus.NOT_FOUND, "");
            var c = comment.get();
            c.setStatus(status);
            c.setUpdated_at(System.currentTimeMillis());
            c.setDeleted_at(0L);
            commentRepository.save(c);

            return new DatabaseQueryResult(true,
                    "update status success",
                    HttpStatus.OK, "");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(
                    false, "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    private void removeOldPin (String postId){
        try {
            currentPinned(postId).ifPresent(comment -> {
                comment.setUpdated_at(System.currentTimeMillis());
                comment.setPin(false);
                commentRepository.save(comment);
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
        }
    }

    private Optional<Comment> currentPinned(String postId){
        return commentRepository.findFirstByPost_idAndStatusAndPinEquals
                (postId, Comment.StatusEnum.SHOW, true);
    }
}
