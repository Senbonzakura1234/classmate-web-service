package com.app.manager.controller;

import com.app.manager.entity.Post;
import com.app.manager.model.payload.request.PostRequest;
import com.app.manager.model.payload.response.MessageResponse;
import com.app.manager.service.interfaceClass.PostService;
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
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@RequestParam(value = "course_id") String course_id){
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getAllByCourse(course_id, currentUser));
    }

    @PostMapping("/post")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<?> post(BindingResult bindingResult,
                 @RequestParam(value = "course_id") String course_id,
                 @Valid @RequestBody PostRequest postRequest) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error",
                            bindingResult.getAllErrors()));
        }

        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = postService
                .save(postRequest, currentUser, course_id);
        if(result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(result.getHttp_status()).body(result);
    }


    @PostMapping("/edit")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<?> edit(BindingResult bindingResult,
                 @RequestParam(value = "message_id") String message_id,
                 @Valid @RequestBody PostRequest postRequest) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error",
                            bindingResult.getAllErrors()));
        }

        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = postService
                .edit(postRequest, message_id, currentUser);
        if(result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(result.getHttp_status()).body(result);
    }


    @PostMapping("/pin")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> edit(@RequestParam(value = "message_id") String message_id) {

        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = postService
                .updatePin(message_id, currentUser);
        if(result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @PostMapping("/updateStatus")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<?> updateStatus(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "status") Post.StatusEnum status
    ) {
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = postService.updateStatus(id, currentUser, status);
        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }
}
