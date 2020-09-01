package com.app.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data/courseCategory")
public class CourseCategoryController {
    @GetMapping("/all")
    public ResponseEntity<?> getAllCategory(){
        return null;
    }
}
