package com.app.manager.controller;

import com.app.manager.model.payload.request.CourseCategoryRequest;
import com.app.manager.model.payload.response.MessageResponse;
import com.app.manager.service.interfaceClass.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/data/courseCategory")
public class CourseCategoryController {
    @Autowired CourseCategoryService courseCategoryService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(courseCategoryService.getAll());
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(
            @Valid @RequestBody CourseCategoryRequest courseCategoryRequest,
                                          BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error",""));
        }

        var result = courseCategoryService.save(courseCategoryRequest);
        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(
            @Valid @RequestBody CourseCategoryRequest courseCategoryRequest,
            @RequestParam(value = "id") String id,
                  BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error",""));
        }

        var result = courseCategoryService
                .update(id ,courseCategoryRequest);
        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }
}
