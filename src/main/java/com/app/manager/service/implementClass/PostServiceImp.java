package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.entity.*;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.PostRequest;
import com.app.manager.model.payload.response.PostResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.PostService;
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
    @Autowired
    PostRepository postRepository;
    @Autowired AttachmentRepository attachmentRepository;
    @Autowired UserRepository userRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired StudentCourseRepository studentCourseRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired CastObject castObject;


    @Override
    public List<PostResponse> getAllByCourse(String courseId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            var course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("course not found"));
            var role = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("role not found"));


            if (!currentUser.getRoles().contains(role) ||
                !course.getUser_id().equals(currentUser.getId()) ||
                studentCourseRepository.findAllByCourse_idAndStatus
                (course.getUser_id(), StudentCourse.StatusEnum.SHOW)
                .stream().noneMatch(studentCourse -> studentCourse.getUser_id()
                .equals(currentUser.getId()))) return new ArrayList<>();

            var collect =
                postRepository.findAllByCourse_idAndStatusAndPinEquals(
                    courseId, Post.StatusEnum.SHOW, false).stream().map(post -> {
                try {
                    var user = userRepository.findById(post.getId());
                    if (user.isEmpty()) return new PostResponse();
                    var profile = castObject.profilePrivate(user.get());
                    var attachments = attachmentRepository
                            .findAllByPost_idAndStatus(post.getId(), Attachment.StatusEnum.SHOW)
                            .stream().map(attachment -> castObject.attachmentModel(attachment))
                            .collect(Collectors.toList());
                    return castObject.postModel(profile, post, attachments);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    return new PostResponse();
                }
            }).collect(Collectors.toList());

            currentPinned(course.getId()).ifPresent(post -> {
                var user = userRepository.findById(post.getId());
                user.ifPresent(value -> {
                    var profile = castObject.profilePrivate(value);
                    var attachments = attachmentRepository
                            .findAllByPost_idAndStatus(post.getId(), Attachment.StatusEnum.SHOW)
                            .stream().map(attachment -> castObject.attachmentModel(attachment))
                            .collect(Collectors.toList());
                    collect.add(0, castObject.postModel(profile, post, attachments));
                });
            });
            return collect;
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ArrayList<>();
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


            if(!course.get().getUser_id().equals(currentUser.get().getId()) ||
                studentCourseRepository.findAllByCourse_idAndStatus
                (course.get().getUser_id(), StudentCourse.StatusEnum.SHOW)
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
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(
            false, "Error: " + e.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR, postRequest);
        }
    }

    @Override
    public DatabaseQueryResult edit(PostRequest postRequest,
                                    String messageId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty()) return new DatabaseQueryResult(
                false, "User not found",
                HttpStatus.NOT_FOUND, postRequest);
            var message = postRepository.findById(messageId);

            if(message.isEmpty()) return new DatabaseQueryResult(
                false, "post not found",
                HttpStatus.NOT_FOUND, postRequest);

            if(!message.get().getUser_id().equals(currentUser.get().getId()))
                return new DatabaseQueryResult(
                    false, "Not your post",
                    HttpStatus.BAD_REQUEST, postRequest);
            var m = message.get();
            m.setContent(postRequest.getContent());
            m.setUpdated_at(System.currentTimeMillis());
            postRepository.save(m);
            attachmentRepository.findAllByPost_idAndStatus
                (m.getId(), Attachment.StatusEnum.SHOW).forEach(attachment -> {
                attachment.setUpdated_at(System.currentTimeMillis());
                attachment.setDeleted_at(System.currentTimeMillis());
                attachment.setStatus(Attachment.StatusEnum.HIDE);
                attachmentRepository.save(attachment);
            });
            postRequest.getAttachmentRequests().forEach(
                attachmentRequest -> attachmentRepository
                .save(castObject.attachmentEntity(m.getId(), attachmentRequest)));
            return new DatabaseQueryResult(true, "edit post success",
                    HttpStatus.OK, postRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(
                    false, "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, postRequest);
        }
    }

    @Override
    public DatabaseQueryResult updatePin(String messageId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty()) return new DatabaseQueryResult(
                    false, "User not found",
                    HttpStatus.NOT_FOUND, "");
            var message = postRepository.findById(messageId);

            if(message.isEmpty()) return new DatabaseQueryResult(
                    false, "post not found",
                    HttpStatus.NOT_FOUND, "");

            if(!message.get().getUser_id().equals(currentUser.get().getId()))
                return new DatabaseQueryResult(
                        false, "Not your post",
                        HttpStatus.BAD_REQUEST, "");
            var m = message.get();
            var isPin = m.isPin();
            if(!isPin) removeOldPin(m.getCourse_id());
            m.setPin(!isPin);
            postRepository.save(m);
            return new DatabaseQueryResult(true, "pin post success",
                    HttpStatus.OK, "");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(
                    false, "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public DatabaseQueryResult updateStatus(String messageId, String currentUsername,
                                            Post.StatusEnum status) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty()) return new DatabaseQueryResult(
                    false, "User not found",
                    HttpStatus.NOT_FOUND, "");
            var message = postRepository.findById(messageId);

            if(message.isEmpty()) return new DatabaseQueryResult(
                    false, "post not found",
                    HttpStatus.NOT_FOUND, "");

            if(!message.get().getUser_id().equals(currentUser.get().getId()))
                return new DatabaseQueryResult(
                        false, "Not your post",
                        HttpStatus.BAD_REQUEST, "");
            var m = message.get();
            m.setPin(false);
            m.setStatus(Post.StatusEnum.HIDE);
            m.setUpdated_at(System.currentTimeMillis());
            m.setDeleted_at(System.currentTimeMillis());
            postRepository.save(m);

            attachmentRepository.findAllByPost_idAndStatus
                    (m.getId(), Attachment.StatusEnum.SHOW).forEach(attachment -> {
                attachment.setUpdated_at(System.currentTimeMillis());
                attachment.setDeleted_at(System.currentTimeMillis());
                attachment.setStatus(Attachment.StatusEnum.HIDE);
                attachmentRepository.save(attachment);
            });
            return new DatabaseQueryResult(true,
                    "remove post success",
                    HttpStatus.OK, "");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(
                    false, "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    private void removeOldPin (String courseId){
        try {
            currentPinned(courseId).ifPresent(message -> {
                message.setUpdated_at(System.currentTimeMillis());
                message.setPin(false);
                postRepository.save(message);
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private Optional<Post> currentPinned(String courseId){
        return postRepository.findFirstByCourse_idAndStatusAndPinEquals
            (courseId, Post.StatusEnum.SHOW, true);
    }
}
