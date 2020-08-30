package com.app.manager.controller;

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

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data/attendance")
public class AttendanceController {
//    private static final Long AttendanceCheckDuration = 1200000L;

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
                    .body(new MessageResponse("Error: Validate Error"));
        }

        var currentUser = SecurityContextHolder
                .getContext().getAuthentication().getName();
        var result = attendanceService
                .studentAttendaneCheck(faceCheckClientRequest, currentUser);

        return result.isSuccess() ? ResponseEntity.ok(result.getDescription()) :
                ResponseEntity.status(result.getHttp_status()).body(result);
    }
}
