package com.app.manager.controller;

import com.app.manager.model.payload.request.ImageLinkRequest;
import com.app.manager.model.payload.response.MessageResponse;
import com.app.manager.model.payload.response.ResultResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.text.MessageFormat;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/service", consumes = "application/json", produces = "application/json")
public class ApiController {
    @Value("${classmate.app.faceCheckServer}")
    private String faceCheckHost;

    @PostMapping("/getImageLink")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody ImageLinkRequest imageLinkRequest) {
        var uri = imageLinkRequest.getImgUri();

        System.out.println(faceCheckHost);

        var headers = new HttpHeaders();
        var entity = new HttpEntity<>(imageLinkRequest, headers);
        var restTemplate = new RestTemplate();
        try {
            var response = restTemplate
                    .exchange(faceCheckHost, HttpMethod.GET, entity, ResultResponse.class);
            var faceCheckResult = response.getBody();

            if (faceCheckResult != null) {
                System.out.println(faceCheckResult.getName());
                System.out.println(faceCheckResult.getPercent());
                System.out.println(faceCheckResult.isValid());
            }else {
                System.out.println("failed");
            }
        } catch (RestClientException e) {
            e.printStackTrace();
            System.out.println("failed");
        }

        return ResponseEntity.ok(new MessageResponse(
                MessageFormat.format(
                "ImageURI - {0} - sent", uri)));
    }
}
