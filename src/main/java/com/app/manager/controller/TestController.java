package com.app.manager.controller;

import com.app.manager.context.Specification.CourseSpecification;
import com.app.manager.context.repository.CourseRepository;
import com.app.manager.entity.Course;
import com.app.manager.model.SearchCriteria;
import com.app.manager.model.payload.CourseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        msGenre.add(new SearchCriteria("name", "", SearchCriteria.SearchOperation.MATCH));
        msGenre.add(new SearchCriteria("status", Course.StatusEnum.PENDING.getValue(),
                SearchCriteria.SearchOperation.EQUAL));

        Sort sortable =
                Sort.by("name").ascending();

        Pageable pageable = PageRequest.of(0, 20, sortable);


        Page<Course> msGenreList = courseRepository.findAll(msGenre, pageable);
        Page<CourseModel> list = msGenreList.map(course -> new CourseModel(course.getCoursecategoryid(),
                course.getName(), course.getDescription(),
                course.getStartdate(), course.getEnddate(), course.getCreatedat()));

        return ResponseEntity.ok(list);
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
