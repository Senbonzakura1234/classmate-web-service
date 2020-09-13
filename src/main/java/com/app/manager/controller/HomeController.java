package com.app.manager.controller;

import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Controller
public class HomeController {
//    @Autowired private GoogleDrive googleDriveservice;
    @Autowired private Drive googleDrive;


    @GetMapping({"/", "/home"})
    public String index() {
        //            googleDriveservice.getService();
//        printAbout();
//        try {
////            getAllGoogleDriveFiles()
////                    .forEach(file -> System.out.println(file.getName() + " - " + file.size()  + " " + file.getId()));
////            System.out.println(createNewFolder("test folder"));
//            setPublic();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return "views/home";
    }

    private List<File> getAllGoogleDriveFiles() throws IOException {
        FileList result = googleDrive.files().list().execute();
        return result.getFiles();
    }


    private String createNewFolder(String folderName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = googleDrive.files().create(fileMetadata).setFields("id").execute();
        return file.getId();
    }

    private String createNewFile(java.io.File fileInput, String mimeType,
                                 String fileName, List<String> parentFolderIds)
            throws IOException {
        File newGGDriveFile = new File();
        newGGDriveFile.setParents(parentFolderIds).setName(fileName);
        FileContent mediaContent = new FileContent(mimeType, fileInput);
        File file = googleDrive.files().create(newGGDriveFile, mediaContent)
                .setFields("id,webViewLink").execute();
        return file.getId();
    }



    private void setPublic(String fileId) throws IOException {
        JsonBatchCallback<Permission> callback = new JsonBatchCallback<>() {
            @Override
            public void onFailure(GoogleJsonError e,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                // Handle error
                System.err.println(e.getMessage());
            }

            @Override
            public void onSuccess(Permission permission,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                System.out.println("Permission ID: " + permission.getId());
            }
        };


        try {
            BatchRequest batch = googleDrive.batch();
            Permission userPermission = new Permission()
                    .setType("anyone")
                    .setRole("writer");
            googleDrive.permissions().create(fileId, userPermission)
                    .setFields("id")
                    .queue(batch, callback);

            batch.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void printAbout() {
        try {
            About about = googleDrive.about().get().setFields("*").execute();
            System.out.println("Total quota (bytes): " + about.getStorageQuota());
            System.out.println("Max upload size (bytes): " + about.getMaxUploadSize());
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }


    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@NotNull List<MultipartFile> files){
        var result = files.stream().map(file -> {
            try {
                HttpServletRequest request = ((ServletRequestAttributes)
                        Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();
                String s = request.getServletContext().getRealPath("");

                System.out.println(file.getOriginalFilename());
                var temp = new java.io.File(s + "/" +
                        file.getOriginalFilename() + "-"
                        + System.currentTimeMillis());


                file.transferTo(temp);


                var folderId = createNewFolder(file.getOriginalFilename());
                var fileId = createNewFile(temp, file.getContentType(),
                        file.getOriginalFilename(), Collections.singletonList(folderId));
                setPublic(fileId);


                if(!temp.delete()){
                    System.out.println("delete fail");
                }
                return new DatabaseQueryResult(true, "okay",
                        HttpStatus.OK, "https://drive.google.com/uc?export=view&id=" + fileId);
            } catch (IOException e) {
                e.printStackTrace();
                return new DatabaseQueryResult(false, "Error: " + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR, "");
            }
        });
        return ResponseEntity.ok(result);
    }
}
