package com.app.manager.controller;

import com.app.manager.context.specification.CourseSpecification;
import com.app.manager.entity.Course;
import com.app.manager.model.SearchCriteria;
import com.app.manager.model.payload.request.CourseRequest;
import com.app.manager.model.payload.request.StudentCourseRequest;
import com.app.manager.model.payload.response.MessageResponse;
import com.app.manager.service.interfaceClass.CourseService;
import com.app.manager.service.interfaceClass.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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
@RequestMapping(value = "/api/data/course")
public class CourseController {
    @Autowired CourseService courseService;
    @Autowired StudentCourseService studentCourseService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "course_category_id", required = false, defaultValue = "")
                    String course_category_id,
            @RequestParam(value = "user_id", required = false, defaultValue = "") String user_id,
            @RequestParam(value = "start_date", required = false, defaultValue = "0") long start_date,
            @RequestParam(value = "end_date", required = false, defaultValue = "0") long end_date,
            @RequestParam(value = "status", required = false) Course.StatusEnum status
    ) {
        var query = new CourseSpecification();
        if(name != null){
            query.add(new SearchCriteria("name", name,
                    SearchCriteria.SearchOperation.MATCH));
        }

        if(status != null && status != Course.StatusEnum.ALL){
            query.add(new SearchCriteria("status", status.getValue(),
                    SearchCriteria.SearchOperation.EQUAL));
        }

        if(course_category_id != null && !course_category_id.isEmpty()){
            query.add(new SearchCriteria("course_category_id", course_category_id,
                    SearchCriteria.SearchOperation.EQUAL));
        }
        if(user_id != null && !user_id.isEmpty()){
            query.add(new SearchCriteria("user_id", user_id,
                    SearchCriteria.SearchOperation.EQUAL));
        }
        if(start_date > 0){
            query.add(new SearchCriteria("start_date", start_date,
                    SearchCriteria.SearchOperation.GREATER_THAN_EQUAL));
        }

        if(end_date > 0){
            query.add(new SearchCriteria("end_date", end_date,
                    SearchCriteria.SearchOperation.LESS_THAN_EQUAL));
        }

        return ResponseEntity.ok(courseService.findAll(query));
    }

    @GetMapping("/followingCourse")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getAllByStudent(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "course_category_id", required = false, defaultValue = "")
                    String course_category_id,
            @RequestParam(value = "user_id", required = false, defaultValue = "") String user_id,
            @RequestParam(value = "start_date", required = false, defaultValue = "0") long start_date,
            @RequestParam(value = "end_date", required = false, defaultValue = "0") long end_date,
            @RequestParam(value = "status", required = false) Course.StatusEnum status
    ) {
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var query = new CourseSpecification();
        if(name != null){
            query.add(new SearchCriteria("name", name,
                    SearchCriteria.SearchOperation.MATCH));
        }

        if(status != null && status != Course.StatusEnum.ALL){
            query.add(new SearchCriteria("status", status.getValue(),
                    SearchCriteria.SearchOperation.EQUAL));
        }

        if(course_category_id != null && !course_category_id.isEmpty()){
            query.add(new SearchCriteria("course_category_id", course_category_id,
                    SearchCriteria.SearchOperation.EQUAL));
        }
        if(user_id != null && !user_id.isEmpty()){
            query.add(new SearchCriteria("user_id", user_id,
                    SearchCriteria.SearchOperation.EQUAL));
        }
        if(start_date > 0){
            query.add(new SearchCriteria("start_date", start_date,
                    SearchCriteria.SearchOperation.GREATER_THAN_EQUAL));
        }

        if(end_date > 0){
            query.add(new SearchCriteria("end_date", end_date,
                    SearchCriteria.SearchOperation.LESS_THAN_EQUAL));
        }

        return ResponseEntity.ok(courseService.findAllByStudent(query, currentUser));
    }

    @GetMapping("/yourCourse")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getAllByTeacher(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "course_category_id", required = false, defaultValue = "")
                    String course_category_id,
            @RequestParam(value = "start_date", required = false, defaultValue = "0") long start_date,
            @RequestParam(value = "end_date", required = false, defaultValue = "0") long end_date,
            @RequestParam(value = "status", required = false) Course.StatusEnum status
    ) {
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var query = new CourseSpecification();
        if(name != null){
            query.add(new SearchCriteria("name", name,
                    SearchCriteria.SearchOperation.MATCH));
        }

        if(status != null && status != Course.StatusEnum.ALL){
            query.add(new SearchCriteria("status", status.getValue(),
                    SearchCriteria.SearchOperation.EQUAL));
        }

        if(course_category_id != null && !course_category_id.isEmpty()){
            query.add(new SearchCriteria("course_category_id", course_category_id,
                    SearchCriteria.SearchOperation.EQUAL));
        }
        if(start_date > 0){
            query.add(new SearchCriteria("start_date", start_date,
                    SearchCriteria.SearchOperation.GREATER_THAN_EQUAL));
        }

        if(end_date > 0){
            query.add(new SearchCriteria("end_date", end_date,
                    SearchCriteria.SearchOperation.LESS_THAN_EQUAL));
        }

        return ResponseEntity.ok(courseService.findAllByTeacher(query, currentUser));
    }

    @GetMapping("/detail")
    @PreAuthorize("hasRole('USER') or hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getOne(@RequestParam(value = "id") String id) {
        var result = courseService.getOne(id);
        return result.isEmpty() ? ResponseEntity
                .status(HttpStatus.NOT_FOUND).body("Not found")
                : ResponseEntity.ok(result.get());
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> save(@Valid @RequestBody CourseRequest courseRequest,
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
        var result = courseService.save(courseRequest, currentUser);
        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> update(@Valid @RequestBody CourseRequest courseRequest,
                                    @RequestParam(value = "id") String id,
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
        var result = courseService.update(courseRequest, id, currentUser);
        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @PostMapping("/updateStatus")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "status") Course.StatusEnum status
    ) {
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = courseService.updateStatus(id, status, currentUser);
        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @PostMapping("/addToCourse")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addToCourse(BindingResult bindingResult,
            @Valid @RequestBody StudentCourseRequest studentCourseRequest) {
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
        var result = studentCourseService
                .addStudentToCourse(studentCourseRequest, currentUser);
        if(result.isSuccess()) return ResponseEntity.ok(result);
                return ResponseEntity.status(result.getHttp_status()).body(result);
    }


    @GetMapping("/allProfileInCourse")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> allProfileInCourse(
            @RequestParam(value = "course_id") String course_id){
//        var currentUser = SecurityContextHolder
//                .getContext().getAuthentication().getName();
        return ResponseEntity.ok(studentCourseService
                .getAllProfileInCourse(course_id));
    }
}
