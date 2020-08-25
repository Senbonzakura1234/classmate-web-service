package com.app.manager.controller;

import com.app.manager.context.specification.CourseSpecification;
import com.app.manager.context.repository.CourseRepository;
import com.app.manager.context.specification.SessionSpecification;
import com.app.manager.entity.Course;
import com.app.manager.entity.Session;
import com.app.manager.model.SearchCriteria;
import com.app.manager.service.interfaceClass.CourseService;
import com.app.manager.service.interfaceClass.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
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

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CourseService courseService;
    @Autowired
    SessionService sessionService;



    @GetMapping("/testError")
    public ResponseEntity<?> TestError() {
        var msGenre = new CourseSpecification();
        msGenre.add(new SearchCriteria("name", "", SearchCriteria.SearchOperation.MATCH));
        msGenre.add(new SearchCriteria("status", Course.StatusEnum.PENDING.getValue(),
                SearchCriteria.SearchOperation.EQUAL));

        var courses = courseRepository.findAll(msGenre);

        List<Course> list = new ArrayList<>();
        for(Course course : courses){
            System.out.println(course.getName());
            list.add(course);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/all")
    public ResponseEntity<?> allAccess() {
        var msGenre = new CourseSpecification();
        msGenre.add(new SearchCriteria("name", "", SearchCriteria.SearchOperation.MATCH));
        msGenre.add(new SearchCriteria("status", Course.StatusEnum.PENDING.getValue(),
                SearchCriteria.SearchOperation.EQUAL));

        Sort sortable =
                Sort.by("name").ascending();

        Pageable pageable = PageRequest.of(0, 20, sortable);


        return ResponseEntity.ok(courseService.findAll(msGenre));
    }

    @GetMapping("/allSession")
    public ResponseEntity<?> allAccessSession() {
        var msGenre = new SessionSpecification();

        msGenre.add(new SearchCriteria("name", "", SearchCriteria.SearchOperation.MATCH));
        msGenre.add(new SearchCriteria("status", Session.StatusEnum.ONGOING.getValue(),
                SearchCriteria.SearchOperation.EQUAL));

        Sort sortable =
                Sort.by("name").ascending();

        Pageable pageable = PageRequest.of(0, 20, sortable);


        return ResponseEntity.ok(sessionService.findAll(msGenre));
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
