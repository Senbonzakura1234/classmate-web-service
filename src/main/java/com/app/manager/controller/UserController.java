package com.app.manager.controller;

import com.app.manager.context.specification.UserSpecification;
import com.app.manager.model.SearchCriteria;
import com.app.manager.model.payload.request.FaceDefinitionClientRequest;
import com.app.manager.model.payload.request.UserProfileRequest;
import com.app.manager.model.payload.response.MessageResponse;
import com.app.manager.service.interfaceClass.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/data/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "username", required = false, defaultValue = "") String username,
            @RequestParam(value = "fullname", required = false, defaultValue = "") String fullname){
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();

        var queryFullname = new UserSpecification();
        var queryUsername = new UserSpecification();

        if(fullname != null){
            queryFullname.add(new SearchCriteria("fullname", fullname,
                    SearchCriteria.SearchOperation.MATCH));
        }

        if(username != null){
            queryUsername.add(new SearchCriteria("username", username,
                    SearchCriteria.SearchOperation.MATCH));
        }

        return ResponseEntity.ok(userService.findAll(
                Specification.where(queryFullname)
                        .or(queryUsername), currentUser));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> profile(@RequestParam(value = "query") String query){
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var profile = userService
                .userProfile(query, currentUser);
        return profile.isEmpty() ? ResponseEntity.badRequest()
                .body(new MessageResponse("Error: user not found",
                    "")) : ResponseEntity.ok(profile.get());
    }

    @PostMapping("/profile/update")
    @PreAuthorize("hasRole('USER') or hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> updateProfile
            (@Valid @RequestBody UserProfileRequest userProfileRequest,
             BindingResult bindingResult){
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
        var result =
                userService.updateProfile(userProfileRequest, currentUser);

        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.badRequest().body(result);
    }

    @PostMapping("/profile/faceCheckDefinition")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> faceCheckDefinition
            (@Valid @RequestBody FaceDefinitionClientRequest faceDefinitionClientRequest,
             BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error",""));
        }
        if(faceDefinitionClientRequest.getFile_ids().isEmpty())
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Null Imagelist",  ""));

        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result =
                userService.faceCheckDefinition(faceDefinitionClientRequest, currentUser);

        return result.isSuccess() ?
                ResponseEntity.ok(result) :
                ResponseEntity.badRequest().body(result);
    }
}
