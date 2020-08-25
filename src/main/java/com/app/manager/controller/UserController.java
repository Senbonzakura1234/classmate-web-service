package com.app.manager.controller;

import com.app.manager.context.specification.UserSpecification;
import com.app.manager.model.SearchCriteria;
import com.app.manager.service.interfaceClass.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data/user")
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
}
