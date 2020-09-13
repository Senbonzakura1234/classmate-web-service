package com.app.manager.drive;

import com.app.manager.model.payload.response.FileUploadResponse;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "RedundantThrows"})
@Component
public class GoogleDriveService {
    @Autowired private Drive googleDrive;
    private static final Logger logger = LoggerFactory.getLogger(GoogleDriveService.class);


    private String createNewDriveFolder(String folderName) throws IOException {
        logger.info("Creating folder name: " + folderName);
        var fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        var file = googleDrive.files().create(fileMetadata)
                .setFields("*").execute();
        return file.getId();
    }

    private File createNewDriveFile(java.io.File fileInput, String mimeType,
                                      String fileName, List<String> parentFolderIds)
            throws IOException {
        logger.info("Creating file name: " + fileName);
        var newGGDriveFile = new File();
        newGGDriveFile.setParents(parentFolderIds).setName(fileName);
        var mediaContent = new FileContent(mimeType, fileInput);
        return googleDrive.files().create(newGGDriveFile, mediaContent)
                .setFields("*").execute();
    }

    private void setPermission(String fileId) throws IOException {
        JsonBatchCallback<Permission> callback = new JsonBatchCallback<>() {
            @Override
            public void onFailure(GoogleJsonError e,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                // Handle error
                logger.info("Error: " + e.getMessage());
                e.getErrors().forEach(errorInfo -> {
                    logger.info(errorInfo.getMessage());
                    logger.info(errorInfo.getReason());
                });
            }

            @Override
            public void onSuccess(Permission permission,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                logger.info("New Permission ID: " +
                    permission.getId() + " for file id = " +  fileId);
            }
        };


        try {
            var batch = googleDrive.batch();
            var userPermission = new Permission()
                    .setType("anyone")
                    .setRole("writer");
            googleDrive.permissions().create(fileId, userPermission)
                    .setFields("id")
                    .queue(batch, callback);

            batch.execute();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Error: " + e.getMessage());
        }
    }


    public Optional<FileUploadResponse> upload(java.io.File fileInput, String mimeType, String fileName){
        try {
            var folderId = createNewDriveFolder(fileName);
            var file = createNewDriveFile(fileInput, mimeType,
                    fileName, Collections.singletonList(folderId));
            setPermission(file.getId());
            return Optional.of(new FileUploadResponse
                    (true, file.getId(), fileName, file.getSize()));
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Error: " + e.getMessage());
            return Optional.of(new FileUploadResponse
                    (false, fileName));
        }
    }


}
