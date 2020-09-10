package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.entity.*;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.PostRequest;
import com.app.manager.model.payload.response.PostResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.PostService;
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
public class PostServiceImp implements PostService {
    @Autowired PostRepository postRepository;
    @Autowired AttachmentRepository attachmentRepository;
    @Autowired UserRepository userRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired StudentCourseRepository studentCourseRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired CastObject castObject;

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImp.class);

    @Override
    public List<PostResponse> getAllByCourse(String courseId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            var course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("course not found"));
            var role = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("role not found"));


            if (!currentUser.getRoles().contains(role) &&
                !course.getUser_id().equals(currentUser.getId()) &&

                studentCourseRepository.findAllByCourse_idAndStatus
                (course.getUser_id(), StudentCourse.StatusEnum.SHOW)
                .stream().noneMatch(studentCourse -> studentCourse.getUser_id()
                .equals(currentUser.getId())))

                return new ArrayList<>();

            return postRepository.findAllByCourse_idAndStatus(
                    courseId, Post.StatusEnum.SHOW).stream().map(post -> {
                try {
                    var user = userRepository.findById(post.getUser_id());
                    if (user.isEmpty()) return new PostResponse();
                    var profile = castObject.profilePrivate(user.get());
                    var attachments = attachmentRepository
                            .findAllByPost_idAndStatus(post.getId(), Attachment.StatusEnum.SHOW)
                            .stream().map(attachment -> castObject.attachmentModel(attachment))
                            .collect(Collectors.toList());
                    return castObject.postModel(profile, post, attachments);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info(e.getMessage());
                    logger.info(e.getCause().getMessage());
                    return new PostResponse();
                }
            }).filter(postResponse -> postResponse.getId() != null).collect(Collectors.toList());
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<PostResponse> getOne(String id, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            var post = postRepository.findFirstByIdAndStatus(id, Post.StatusEnum.SHOW)
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
            return Optional.empty();

            var user = userRepository.findById(post.getId());
            if (user.isEmpty()) return Optional.empty();
            var profile = castObject.profilePrivate(user.get());
            var attachments = attachmentRepository
                    .findAllByPost_idAndStatus(post.getId(), Attachment.StatusEnum.SHOW)
                    .stream().map(attachment -> castObject.attachmentModel(attachment))
                    .collect(Collectors.toList());

            return Optional.of(castObject.postModel(profile, post, attachments));
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return Optional.empty();
        }
    }

    @Override
    public DatabaseQueryResult save(PostRequest postRequest,
                                    String currentUsername, String courseId) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty()) return new DatabaseQueryResult(
                    false, "User not found",
                    HttpStatus.NOT_FOUND, postRequest);

            var course = courseRepository.findById(courseId);
            if(course.isEmpty()) return new DatabaseQueryResult(
                    false, "course not found",
                    HttpStatus.NOT_FOUND, postRequest);


            if(!course.get().getUser_id().equals(currentUser.get().getId()) &&
                studentCourseRepository.findAllByCourse_idAndStatus
                (course.get().getId(), StudentCourse.StatusEnum.SHOW)
                .stream().noneMatch(studentCourse -> studentCourse.getUser_id()
                .equals(currentUser.get().getId()))) return new DatabaseQueryResult(
                    false, "Not your course",
                    HttpStatus.BAD_REQUEST, postRequest);
            var post = castObject.postEntity(
                    currentUser.get().getId(), course.get().getId(),
                    postRequest);
            postRepository.save(post);
            postRequest.getAttachmentRequests()
                    .forEach(attachmentRequest -> attachmentRepository
                    .save(castObject.attachmentEntity(post.getId(), attachmentRequest)));
            return new DatabaseQueryResult(true, "post post success",
                    HttpStatus.OK, postRequest);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(
            false, "Error: " + e.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR, postRequest);
        }
    }

    @Override
    public DatabaseQueryResult edit(PostRequest postRequest,
                                    String postId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty()) return new DatabaseQueryResult(
                false, "User not found",
                HttpStatus.NOT_FOUND, postRequest);
            var post = postRepository
                    .findFirstByIdAndStatus(postId, Post.StatusEnum.SHOW);

            if(post.isEmpty()) return new DatabaseQueryResult(
                false, "post not found",
                HttpStatus.NOT_FOUND, postRequest);

            if(!post.get().getUser_id().equals(currentUser.get().getId()))
                return new DatabaseQueryResult(
                    false, "Not your post",
                    HttpStatus.BAD_REQUEST, postRequest);
            var p = post.get();
            p.setContent(postRequest.getContent());
            p.setUpdated_at(System.currentTimeMillis());
            postRepository.save(p);
            attachmentRepository.findAllByPost_idAndStatus
                (p.getId(), Attachment.StatusEnum.SHOW).forEach(attachment -> {
                attachment.setUpdated_at(System.currentTimeMillis());
                attachment.setDeleted_at(System.currentTimeMillis());
                attachment.setStatus(Attachment.StatusEnum.HIDE);
                attachmentRepository.save(attachment);
            });
            postRequest.getAttachmentRequests().forEach(
                attachmentRequest -> attachmentRepository
                .save(castObject.attachmentEntity(p.getId(), attachmentRequest)));
            return new DatabaseQueryResult(true, "edit post success",
                    HttpStatus.OK, postRequest);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(
                    false, "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, postRequest);
        }
    }

    @Override
    public DatabaseQueryResult updatePin(String postId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty()) return new DatabaseQueryResult(
                    false, "User not found",
                    HttpStatus.NOT_FOUND, "");

            var post = postRepository
                    .findFirstByIdAndStatus(postId, Post.StatusEnum.SHOW);
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
            var p = post.get();
            var isPin = p.isPin();
            if(!isPin) removeOldPin(p.getCourse_id());
            p.setPin(!isPin);
            p.setUpdated_at(System.currentTimeMillis());
            postRepository.save(p);
            return new DatabaseQueryResult(true, "pin post success",
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

    @Override
    public DatabaseQueryResult delete(String postId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty()) return new DatabaseQueryResult(
                    false, "User not found",
                    HttpStatus.NOT_FOUND, "");

            var post = postRepository
                    .findFirstByIdAndStatus(postId, Post.StatusEnum.SHOW);
            if(post.isEmpty()) return new DatabaseQueryResult(
                    false, "post not found",
                    HttpStatus.NOT_FOUND, "");

            var course = courseRepository
                    .findById(post.get().getCourse_id());
            if(course.isEmpty()) return new DatabaseQueryResult(
                    false, "course not found",
                    HttpStatus.NOT_FOUND, "");

            if(!post.get().getUser_id().equals(currentUser.get().getId())
                && !course.get().getUser_id().equals(currentUser.get().getId()))
                return new DatabaseQueryResult(false,
                    "you have no authority to delete this post",
                    HttpStatus.BAD_REQUEST, "");

            var p = post.get();
            p.setStatus(Post.StatusEnum.HIDE);
            p.setUpdated_at(System.currentTimeMillis());
            p.setDeleted_at(System.currentTimeMillis());
            postRepository.save(p);

            return new DatabaseQueryResult(true,
                    "delete post success",
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

    @Override
    public DatabaseQueryResult updateStatus(String postId, Post.StatusEnum status) {
        try {
            var post = postRepository.findById(postId);
            if(post.isEmpty()) return new DatabaseQueryResult(false,
                    "post not found", HttpStatus.NOT_FOUND, "");

            var p = post.get();
            p.setStatus(status);
            p.setUpdated_at(System.currentTimeMillis());
            p.setDeleted_at(0L);
            postRepository.save(p);

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

    private void removeOldPin (String courseId){
        try {
            currentPinned(courseId).ifPresent(post -> {
                post.setUpdated_at(System.currentTimeMillis());
                post.setPin(false);
                postRepository.save(post);
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
        }
    }

    private Optional<Post> currentPinned(String courseId){
        return postRepository.findFirstByCourse_idAndStatusAndPinEquals
            (courseId, Post.StatusEnum.SHOW, true);
    }
}
