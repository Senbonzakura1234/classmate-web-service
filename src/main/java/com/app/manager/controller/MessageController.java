package com.app.manager.controller;

import com.app.manager.entity.Message;
import com.app.manager.model.payload.request.CourseMessageRequest;
import com.app.manager.model.payload.response.MessageResponse;
import com.app.manager.service.interfaceClass.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data/message")
public class MessageController {
    @Autowired MessageService messageService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@RequestParam(value = "course_id") String course_id){
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        return ResponseEntity.ok(messageService.getAllByCourse(course_id, currentUser));
    }

    @PostMapping("/post")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<?> post(BindingResult bindingResult,
                 @RequestParam(value = "course_id") String course_id,
                 @Valid @RequestBody CourseMessageRequest courseMessageRequest) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error"));
        }

        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = messageService
                .saveMessage(courseMessageRequest, currentUser, course_id);
        if(result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(result.getHttp_status()).body(result);
    }


    @PostMapping("/edit")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<?> edit(BindingResult bindingResult,
                 @RequestParam(value = "message_id") String message_id,
                 @Valid @RequestBody CourseMessageRequest courseMessageRequest) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error"));
        }

        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = messageService
                .editMessage(courseMessageRequest, message_id, currentUser);
        if(result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(result.getHttp_status()).body(result);
    }


    @PostMapping("/pin")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> edit(@RequestParam(value = "message_id") String message_id) {

        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = messageService
                .updatePinnedMessage(message_id, currentUser);
        if(result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @PostMapping("/updateStatus")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<?> updateStatus(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "status") Message.StatusEnum status
    ) {
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = messageService.updateStatusMessage(id, currentUser, status);
        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }
}
