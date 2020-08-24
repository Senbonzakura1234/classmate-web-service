package com.app.manager.controller;

import com.app.manager.context.specification.CourseSpecification;
import com.app.manager.context.specification.SessionSpecification;
import com.app.manager.entity.Course;
import com.app.manager.entity.Session;
import com.app.manager.model.SearchCriteria;
import com.app.manager.model.payload.CourseModel;
import com.app.manager.model.payload.SessionModel;
import com.app.manager.model.payload.response.MessageResponse;
import com.app.manager.service.interfaceClass.CourseService;
import com.app.manager.service.interfaceClass.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data/session")
public class SessionController {
    @Autowired
    SessionService sessionService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "courseid", required = false, defaultValue = "0") String courseid,
            @RequestParam(value = "userid", required = false, defaultValue = "0") String userid,
            @RequestParam(value = "status", required = false) Session.StatusEnum status,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy
    ) {
        var query = new SessionSpecification();
        if(name != null){
            query.add(new SearchCriteria("name", name, SearchCriteria.SearchOperation.MATCH));
        }

        if(status != null && status != Session.StatusEnum.ALL){
            query.add(new SearchCriteria("status", status.getValue(), SearchCriteria.SearchOperation.EQUAL));
        }

        if(courseid != null && !courseid.isEmpty()){
            query.add(new SearchCriteria("courseid", courseid,
                    SearchCriteria.SearchOperation.EQUAL));
        }
        if(userid != null && !userid.isEmpty()){
            query.add(new SearchCriteria("userid", userid,
                    SearchCriteria.SearchOperation.EQUAL));
        }
        Sort sortable = sort.equals("DESC")?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page <= 0? 0: page - 1, size, sortable);


        return ResponseEntity.ok(sessionService.findAll(query, pageable));
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getOne(@RequestParam(value = "id") String id) {
        var result = sessionService.getOne(id);
        if(result.isEmpty()) return ResponseEntity.badRequest().body("Not found");
        return ResponseEntity.ok(result.get());
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> save(@Valid @RequestBody SessionModel sessionModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(System.out::println);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Validate Error"));
        }
        var result = sessionService.save(sessionModel);
        return result.isSuccess() ? ResponseEntity.ok(result.getDescription()) :
                ResponseEntity.badRequest().body(result.getDescription());
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> update(@Valid @RequestBody SessionModel sessionModel,
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
        var result = sessionService.update(sessionModel, id);
        return result.isSuccess() ? ResponseEntity.ok(result.getDescription()) :
                ResponseEntity.badRequest().body(result.getDescription());
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> delete(@RequestParam(value = "id") String id) {
        var result = sessionService.delete(id);
        return result.isSuccess() ? ResponseEntity.ok(result.getDescription()) :
                ResponseEntity.badRequest().body(result.getDescription());
    }
}
