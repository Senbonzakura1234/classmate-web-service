package com.app.manager.controller;

import com.app.manager.model.payload.request.AttendanceCheckRequest;
import com.app.manager.model.payload.request.FaceCheckClientRequest;
import com.app.manager.model.payload.response.MessageResponse;
import com.app.manager.service.interfaceClass.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/data/attendance")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @PostMapping("/check")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> save(@Valid @RequestBody
                                  FaceCheckClientRequest faceCheckClientRequest,
                                  BindingResult bindingResult){
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
        var result = attendanceService
                .studentAttendaneCheck(faceCheckClientRequest, currentUser);

        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @PostMapping("/checkAll")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> checkAll(
            @Valid @RequestBody List<AttendanceCheckRequest> attendanceCheckRequests,
            @RequestParam(value = "session_id") String session_id,
            BindingResult bindingResult){
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
        var result = attendanceService
                .teacherAttendaneCheck(attendanceCheckRequests,
                        currentUser, session_id, false);

        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @PostMapping("/checkOne")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> checkOne(
            @Valid @RequestBody AttendanceCheckRequest attendanceCheckRequest,
            @RequestParam(value = "session_id") String session_id,
            BindingResult bindingResult){
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
        var result = attendanceService
                .teacherAttendaneCheckOne(attendanceCheckRequest,
                        currentUser, session_id);

        return result.isSuccess() ? ResponseEntity.ok(result) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }

    @GetMapping("/checkResult")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> checkResult(
            @RequestParam(value = "session_id") String session_id){
        return ResponseEntity.ok(attendanceService
                .getAttendanceResult(session_id));
    }

    @GetMapping("/listCheckResult")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> listCheckResult(
            @RequestParam(value = "course_id") String course_id){
        return ResponseEntity.ok(attendanceService
                .getListAttendanceResult(course_id));
    }
}
