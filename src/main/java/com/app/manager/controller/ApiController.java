package com.app.manager.controller;

import com.app.manager.model.payload.request.ImageLinkRequest;
import com.app.manager.model.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.MessageFormat;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/service", consumes = "application/json", produces = "application/json")
public class ApiController {
    @PostMapping("/getImageLink")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody ImageLinkRequest imageLinkRequest) {
        var uri = imageLinkRequest.getImgUri();
        return ResponseEntity.ok(new MessageResponse(
                MessageFormat.format(
                "ImageURI - {0} - sent", uri)));
    }
}
