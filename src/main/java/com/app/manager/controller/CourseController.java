package com.app.manager.controller;

import com.app.manager.context.specification.CourseSpecification;
import com.app.manager.entity.Course;
import com.app.manager.model.SearchCriteria;
import com.app.manager.model.payload.CourseModel;
import com.app.manager.model.payload.request.StudentCourseRequest;
import com.app.manager.model.payload.response.MessageResponse;
import com.app.manager.service.interfaceClass.CourseService;
import com.app.manager.service.interfaceClass.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data/course")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    StudentCourseService studentCourseService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "coursecategoryid", required = false, defaultValue = "") String coursecategoryid,
            @RequestParam(value = "userid", required = false, defaultValue = "") String userid,
            @RequestParam(value = "startdate", required = false, defaultValue = "0") long startdate,
            @RequestParam(value = "enddate", required = false, defaultValue = "0") long enddate,
            @RequestParam(value = "status", required = false) Course.StatusEnum status,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy
    ) {
        var query = new CourseSpecification();
        if(name != null){
            query.add(new SearchCriteria("name", name, SearchCriteria.SearchOperation.MATCH));
        }

        if(status != null && status != Course.StatusEnum.ALL){
            query.add(new SearchCriteria("status", status.getValue(), SearchCriteria.SearchOperation.EQUAL));
        }

        if(coursecategoryid != null && !coursecategoryid.isEmpty()){
            query.add(new SearchCriteria("coursecategoryid", coursecategoryid,
                    SearchCriteria.SearchOperation.EQUAL));
        }
        if(userid != null && !userid.isEmpty()){
            query.add(new SearchCriteria("userid", userid,
                    SearchCriteria.SearchOperation.EQUAL));
        }
        if(startdate > 0){
            query.add(new SearchCriteria("startdate", startdate, SearchCriteria.SearchOperation.GREATER_THAN_EQUAL));
        }

        if(enddate > 0){
            query.add(new SearchCriteria("enddate", enddate, SearchCriteria.SearchOperation.LESS_THAN_EQUAL));
        }

        Sort sortable = sort.equals("DESC")?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page <= 0? 0: page - 1, size, sortable);

        return ResponseEntity.ok(courseService.findAll(query, pageable));
    }

    @GetMapping("/detail")
    @PreAuthorize("hasRole('USER') or hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getOne(@RequestParam(value = "id") String id) {
        var result = courseService.getOne(id);
        if(result.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND).body("Not Found");
        return ResponseEntity.ok(result.get());
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> save(@Valid @RequestBody CourseModel courseModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error"));
        }
        var result = courseService.save(courseModel);
        return result.isSuccess() ? ResponseEntity.ok(result.getDescription()) :
                ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> update(@Valid @RequestBody CourseModel courseModel,
                                    @RequestParam(value = "id") String id,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error"));
        }
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = courseService.update(courseModel, id, currentUser);
        return result.isSuccess() ? ResponseEntity.ok(result.getDescription()) :
                ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> delete(@RequestParam(value = "id") String id) {
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = courseService.delete(id, currentUser);
        return result.isSuccess() ? ResponseEntity.ok(result.getDescription()) :
                ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @PostMapping("/addToCourse")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addToCourse(
            @Valid @RequestBody StudentCourseRequest studentCourseRequest) {
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = studentCourseService
                .addStudentToCourse(studentCourseRequest, currentUser);
        if(result.isSuccess()) return ResponseEntity.ok(result);
                return ResponseEntity.status(result.getHttpStatus()).body(result);
    }
}
