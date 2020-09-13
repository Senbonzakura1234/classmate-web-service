package com.app.manager.drive;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
public class GoogleDrive {
    @Autowired private GoogleCredential googleCredential;
    private static final Logger logger = LoggerFactory.getLogger(GoogleDrive.class);

    @Bean
    public Drive getService() throws GeneralSecurityException, IOException {
        logger.info("getting service");
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT,
                JacksonFactory.getDefaultInstance(), googleCredential)
                .build();
    }

    @Bean
    public GoogleCredential googleCredential() throws GeneralSecurityException, IOException {
        logger.info("getting credential");
        Collection<String> elenco = new ArrayList<>();
        elenco.add("https://www.googleapis.com/auth/drive");
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        return new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId("classmate@cloud-drive-storage-289307.iam.gserviceaccount.com")
                .setServiceAccountScopes(elenco)
                .setServiceAccountPrivateKeyFromP12File(new File(
                        "src/main/resources/cloud-drive-storage-289307-483108576aac.p12"))
                .build();
    }

}
