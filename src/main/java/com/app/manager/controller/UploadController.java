package com.app.manager.controller;

import com.app.manager.drive.GoogleDriveService;
import com.app.manager.model.payload.response.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/data/upload")
public class UploadController {
    @Autowired
    GoogleDriveService googleDriveService;
//    private static final String shareCloudHost = "https://drive.google.com/uc?export=view&id=";

    private String getFileTransferPath(String fileName){
        return MessageFormat.format("{0}/{1}-{2}", ((ServletRequestAttributes)
                        Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest().getServletContext().getRealPath(""), fileName,
                System.currentTimeMillis());
    }

    @PostMapping(value = "/googleDrive", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> googleDrive(List<MultipartFile> uploadFiles){
        if(uploadFiles == null || uploadFiles.isEmpty())
            return ResponseEntity.badRequest().body("Please upload valid file");

        return ResponseEntity.ok(uploadFiles.stream().map(uploadFile -> {
            try {
                if(uploadFile.getOriginalFilename() == null ||
                        uploadFile.getOriginalFilename().isEmpty() ||
                        uploadFile.getOriginalFilename().isBlank())
                    return new FileUploadResponse(false, "file error");
                var temporaryFile = new File(getFileTransferPath(uploadFile.getOriginalFilename()));
                uploadFile.transferTo(temporaryFile);

                var returnFile = googleDriveService.upload(temporaryFile,
                        uploadFile.getContentType(), uploadFile.getOriginalFilename())
                        .orElseGet(() -> new FileUploadResponse(
                                false, uploadFile.getOriginalFilename()));

                if(!temporaryFile.delete()){
                    System.out.println("delete temp file fail");
                }
                return returnFile;
            } catch (IOException e) {
                e.printStackTrace();
                return new FileUploadResponse(
                        false, uploadFile.getOriginalFilename());
            }
        }).collect(Collectors.toList()));
    }
}
