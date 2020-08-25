package com.app.manager.controller;

import com.app.manager.context.specification.SessionSpecification;
import com.app.manager.entity.Session;
import com.app.manager.model.SearchCriteria;
import com.app.manager.model.payload.request.SessionRequest;
import com.app.manager.model.payload.response.MessageResponse;
import com.app.manager.service.interfaceClass.SessionService;
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
@RequestMapping("/api/data/session")
public class SessionController {
    @Autowired
    SessionService sessionService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "courseid", required = false, defaultValue = "0") String courseid,
            @RequestParam(value = "userid", required = false, defaultValue = "0") String userid,
            @RequestParam(value = "status", required = false) Session.StatusEnum status
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

        return ResponseEntity.ok(sessionService.findAll(query));
    }

    @GetMapping("/detail")
    @PreAuthorize("hasRole('USER') or hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getOne(@RequestParam(value = "id") String id) {
        var result = sessionService.getOne(id);
        if(result.isEmpty())  return ResponseEntity
                .status(HttpStatus.NOT_FOUND).body("Not Found");
        return ResponseEntity.ok(result.get());
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> save(@Valid @RequestBody SessionRequest sessionRequest, BindingResult bindingResult) {
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
        var result = sessionService.save(sessionRequest, currentUser);
        return result.isSuccess() ? ResponseEntity.ok(result.getDescription()) :
                ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> update(@Valid @RequestBody SessionRequest sessionRequest,
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
        var result = sessionService.update(sessionRequest, id, currentUser);
        return result.isSuccess() ? ResponseEntity.ok(result.getDescription()) :
                ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> delete(@RequestParam(value = "id") String id) {
        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = sessionService.delete(id, currentUser);
        return result.isSuccess() ? ResponseEntity.ok(result.getDescription()) :
                ResponseEntity.status(result.getHttpStatus()).body(result);
    }
}
