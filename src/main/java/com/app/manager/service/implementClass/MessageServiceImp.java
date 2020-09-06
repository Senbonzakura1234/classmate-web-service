package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.entity.*;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.CourseMessageRequest;
import com.app.manager.model.payload.response.CourseMessageResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class MessageServiceImp implements MessageService {
    @Autowired MessageRepository messageRepository;
    @Autowired AttachmentRepository attachmentRepository;
    @Autowired UserRepository userRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired StudentCourseRepository studentCourseRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired CastObject castObject;


    @Override
    public List<CourseMessageResponse> getAllByCourse(String courseId, String currentUsername) {
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
                messageRepository.findAllByCourse_idAndStatusAndPinEquals(
                    courseId, Message.StatusEnum.SHOW, false).stream().map(message -> {
                try {
                    var user = userRepository.findById(message.getId());
                    if (user.isEmpty()) return new CourseMessageResponse();
                    var profile = castObject.profilePrivate(user.get());
                    var attachments = attachmentRepository
                            .findAllByMessage_idAndStatus(message.getId(), Attachment.StatusEnum.SHOW)
                            .stream().map(attachment -> castObject.attachmentModel(attachment))
                            .collect(Collectors.toList());
                    return castObject.messageModel(profile, message, attachments);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    return new CourseMessageResponse();
                }
            }).collect(Collectors.toList());

            currentPinned(course.getId()).ifPresent(message -> {
                var user = userRepository.findById(message.getId());
                user.ifPresent(value -> {
                    var profile = castObject.profilePrivate(value);
                    var attachments = attachmentRepository
                            .findAllByMessage_idAndStatus(message.getId(), Attachment.StatusEnum.SHOW)
                            .stream().map(attachment -> castObject.attachmentModel(attachment))
                            .collect(Collectors.toList());
                    collect.add(0, castObject.messageModel(profile, message, attachments));
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
    public DatabaseQueryResult saveMessage(CourseMessageRequest courseMessageRequest,
                                           String currentUsername, String courseId) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty()) return new DatabaseQueryResult(
                    false, "User not found",
                    HttpStatus.NOT_FOUND, courseMessageRequest);

            var course = courseRepository.findById(courseId);
            if(course.isEmpty()) return new DatabaseQueryResult(
                    false, "course not found",
                    HttpStatus.NOT_FOUND, courseMessageRequest);


            if(!course.get().getUser_id().equals(currentUser.get().getId()) ||
                studentCourseRepository.findAllByCourse_idAndStatus
                (course.get().getUser_id(), StudentCourse.StatusEnum.SHOW)
                .stream().noneMatch(studentCourse -> studentCourse.getUser_id()
                .equals(currentUser.get().getId()))) return new DatabaseQueryResult(
                    false, "Not your course",
                    HttpStatus.BAD_REQUEST, courseMessageRequest);
            var message = castObject.messageEntity(
                    currentUser.get().getId(), course.get().getId(),
                    courseMessageRequest);
            messageRepository.save(message);
            courseMessageRequest.getAttachmentRequests()
                    .forEach(attachmentRequest -> attachmentRepository
                    .save(castObject.attachmentEntity(message.getId(), attachmentRequest)));
            return new DatabaseQueryResult(true, "post message success",
                    HttpStatus.OK, courseMessageRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(
            false, "Error: " + e.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public DatabaseQueryResult editMessage(CourseMessageRequest courseMessageRequest,
                                           String messageId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty()) return new DatabaseQueryResult(
                false, "User not found",
                HttpStatus.NOT_FOUND, courseMessageRequest);
            var message = messageRepository.findById(messageId);

            if(message.isEmpty()) return new DatabaseQueryResult(
                false, "message not found",
                HttpStatus.NOT_FOUND, courseMessageRequest);

            if(!message.get().getUser_id().equals(currentUser.get().getId()))
                return new DatabaseQueryResult(
                    false, "Not your message",
                    HttpStatus.BAD_REQUEST, courseMessageRequest);
            var m = message.get();
            m.setContent(courseMessageRequest.getContent());
            m.setUpdated_at(System.currentTimeMillis());
            messageRepository.save(m);
            attachmentRepository.findAllByMessage_idAndStatus
                (m.getId(), Attachment.StatusEnum.SHOW).forEach(attachment -> {
                attachment.setUpdated_at(System.currentTimeMillis());
                attachment.setDeleted_at(System.currentTimeMillis());
                attachment.setStatus(Attachment.StatusEnum.HIDE);
                attachmentRepository.save(attachment);
            });
            courseMessageRequest.getAttachmentRequests().forEach(
                attachmentRequest -> attachmentRepository
                .save(castObject.attachmentEntity(m.getId(), attachmentRequest)));
            return new DatabaseQueryResult(true, "edit message success",
                    HttpStatus.OK, courseMessageRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(
                    false, "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public DatabaseQueryResult updatePinnedMessage(String messageId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty()) return new DatabaseQueryResult(
                    false, "User not found",
                    HttpStatus.NOT_FOUND, "");
            var message = messageRepository.findById(messageId);

            if(message.isEmpty()) return new DatabaseQueryResult(
                    false, "message not found",
                    HttpStatus.NOT_FOUND, "");

            if(!message.get().getUser_id().equals(currentUser.get().getId()))
                return new DatabaseQueryResult(
                        false, "Not your message",
                        HttpStatus.BAD_REQUEST, "");
            var m = message.get();
            var isPin = m.isPin();
            if(!isPin) removeOldPin(m.getCourse_id());
            m.setPin(!isPin);
            messageRepository.save(m);
            return new DatabaseQueryResult(true, "pin message success",
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
    public DatabaseQueryResult updateStatusMessage(String messageId, String currentUsername,
                                                   Message.StatusEnum status) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty()) return new DatabaseQueryResult(
                    false, "User not found",
                    HttpStatus.NOT_FOUND, "");
            var message = messageRepository.findById(messageId);

            if(message.isEmpty()) return new DatabaseQueryResult(
                    false, "message not found",
                    HttpStatus.NOT_FOUND, "");

            if(!message.get().getUser_id().equals(currentUser.get().getId()))
                return new DatabaseQueryResult(
                        false, "Not your message",
                        HttpStatus.BAD_REQUEST, "");
            var m = message.get();
            m.setPin(false);
            m.setStatus(Message.StatusEnum.HIDE);
            m.setUpdated_at(System.currentTimeMillis());
            m.setDeleted_at(System.currentTimeMillis());
            messageRepository.save(m);

            attachmentRepository.findAllByMessage_idAndStatus
                    (m.getId(), Attachment.StatusEnum.SHOW).forEach(attachment -> {
                attachment.setUpdated_at(System.currentTimeMillis());
                attachment.setDeleted_at(System.currentTimeMillis());
                attachment.setStatus(Attachment.StatusEnum.HIDE);
                attachmentRepository.save(attachment);
            });
            return new DatabaseQueryResult(true,
                    "remove message success",
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
                messageRepository.save(message);
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private Optional<Message> currentPinned(String courseId){
        return messageRepository.findFirstByCourse_idAndStatusAndPinEquals
            (courseId, Message.StatusEnum.SHOW, true);
    }
}
