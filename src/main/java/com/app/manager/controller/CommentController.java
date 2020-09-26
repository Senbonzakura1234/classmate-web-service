package com.app.manager.controller;

import com.app.manager.entity.Comment;
import com.app.manager.model.payload.request.CommentRequest;
import com.app.manager.model.payload.response.MessageResponse;
import com.app.manager.service.interfaceClass.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/data/comment")
public class CommentController {
    @Autowired CommentService commentService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@RequestParam(value = "post_id") String post_id){
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.getAllByPost(post_id, currentUser));
    }

    @PostMapping("/post")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<?> post(
                                  @RequestParam(value = "post_id") String post_id,
                                  @Valid @RequestBody CommentRequest commentRequest,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error",""));
        }

        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = commentService
                .save(commentRequest, currentUser, post_id);
        if(result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<?> edit(
                                  @RequestParam(value = "comment_id") String comment_id,
                                  @Valid @RequestBody CommentRequest commentRequest,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error",""));
        }

        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = commentService
                .edit(commentRequest, comment_id, currentUser);
        if(result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @PostMapping("/pin")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> edit(@RequestParam(value = "comment_id") String comment_id) {

        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = commentService
                .updatePin(comment_id, currentUser);
        if(result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<?> delete(@RequestParam(value = "id") String id) {
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = commentService.delete(id, currentUser);
        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @PostMapping("/updateStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(@RequestParam(value = "id") String id,
                @RequestParam(value = "status") Comment.StatusEnum status) {
        var result = commentService.updateStatus(id, status);
        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }
}
