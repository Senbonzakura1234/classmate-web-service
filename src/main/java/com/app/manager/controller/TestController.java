package com.app.manager.controller;

import com.app.manager.context.Specification.CourseSpecification;
import com.app.manager.context.repository.CourseRepository;
import com.app.manager.entity.Course;
import com.app.manager.model.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    CourseRepository courseRepository;

    @GetMapping("/all")
    public ResponseEntity<?> allAccess() {
        var msGenre = new CourseSpecification();
        msGenre.add(new SearchCriteria("name", "btb", SearchCriteria.SearchOperation.MATCH));
        msGenre.add(new SearchCriteria("status", Course.StatusEnum.PENDING.getValue(),
                SearchCriteria.SearchOperation.EQUAL));

        var msGenreList = courseRepository.findAll(msGenre);
//        msGenreList.forEach(System.out::println);
        return ResponseEntity.ok(msGenreList);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
