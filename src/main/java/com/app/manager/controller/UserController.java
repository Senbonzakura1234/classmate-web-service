package com.app.manager.controller;

import com.app.manager.context.specification.UserSpecification;
import com.app.manager.model.SearchCriteria;
import com.app.manager.service.interfaceClass.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            @RequestParam(value = "fullname", required = false, defaultValue = "") String fullname,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(name = "sortBy", required = false, defaultValue = "createdat") String sortBy){
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

        var sortable = sort.equals("DESC")?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page <= 0? 0: page - 1, size, sortable);
        return ResponseEntity.ok(userService.findAll(
                Specification.where(queryFullname)
                        .or(queryUsername), pageable, currentUser));
    }
}
