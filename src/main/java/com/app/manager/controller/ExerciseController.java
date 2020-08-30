package com.app.manager.controller;

import com.app.manager.context.specification.ExerciseSpecification;
import com.app.manager.entity.Exercise;
import com.app.manager.model.SearchCriteria;
import com.app.manager.service.interfaceClass.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data/exercise")
public class ExerciseController {
    @Autowired ExerciseService exerciseService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "session_id", required = false, defaultValue = "") String session_id,
            @RequestParam(value = "content", required = false, defaultValue = "") String content,
            @RequestParam(value = "status", required = false) Exercise.StatusEnum status
    ) {
        var query = new ExerciseSpecification();
        if(title != null){
            query.add(new SearchCriteria("title", title,
                    SearchCriteria.SearchOperation.MATCH));
        }

        if(session_id != null && !session_id.isEmpty()){
            query.add(new SearchCriteria("session_id", session_id,
                    SearchCriteria.SearchOperation.EQUAL));
        }

        if(content != null && !content.isEmpty()){
            query.add(new SearchCriteria("content", content,
                    SearchCriteria.SearchOperation.EQUAL));
        }

        if(status != null && status != Exercise.StatusEnum.ALL){
            query.add(new SearchCriteria("status", status.getValue(),
                    SearchCriteria.SearchOperation.EQUAL));
        }

        return ResponseEntity.ok(exerciseService.findAll(query));
    }

}
