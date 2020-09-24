package com.app.manager.controller;

import com.app.manager.context.repository.UserRepository;
import com.app.manager.entity.EVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Controller
public class HomeController {
//    @Autowired private GoogleDrive googleDriveservice;
//    @Autowired private Drive googleDrive;
    @Autowired
    private UserRepository userRepository;


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

//        pingHost("localhost", 5000, 5000);
        userRepository.findAll().forEach(user -> {
            if(user.getUsername().contains("Teacher")){
                user.setProfile_visibility(EVisibility.PUBLIC);
                userRepository.save(user);
            }else if(user.getUsername().contains("Student")){
                user.setProfile_visibility(EVisibility.COURSE);
                userRepository.save(user);
            }else {
                user.setProfile_visibility(EVisibility.PRIVATE);
                userRepository.save(user);
            }
        });
        return "views/home";
    }

//    private boolean pingHost(String host, int port, int timeout) {
//        try (Socket socket = new Socket()) {
//            socket.connect(new InetSocketAddress(host, port), timeout);
//            return true;
//        } catch (IOException e) {
//            return false; // Either timeout or unreachable or failed DNS lookup.
//        }
//    }

//    private List<File> getAllGoogleDriveFiles() throws IOException {
//        FileList result = googleDrive.files().list().execute();
//        return result.getFiles();
//    }
//
//
//    private String createNewFolder(String folderName) throws IOException {
//        File fileMetadata = new File();
//        fileMetadata.setName(folderName);
//        fileMetadata.setMimeType("application/vnd.google-apps.folder");
//
//        File file = googleDrive.files().create(fileMetadata).setFields("id").execute();
//        return file.getId();
//    }
//
//    private String createNewFile(java.io.File fileInput, String mimeType,
//                                 String fileName, List<String> parentFolderIds)
//            throws IOException {
//        File newGGDriveFile = new File();
//        newGGDriveFile.setParents(parentFolderIds).setName(fileName);
//        FileContent mediaContent = new FileContent(mimeType, fileInput);
//        File file = googleDrive.files().create(newGGDriveFile, mediaContent)
//                .setFields("id,webViewLink").execute();
//        return file.getId();
//    }
//
//
//
//    private void setPublic(String fileId) throws IOException {
//        JsonBatchCallback<Permission> callback = new JsonBatchCallback<>() {
//            @Override
//            public void onFailure(GoogleJsonError e,
//                                  HttpHeaders responseHeaders)
//                    throws IOException {
//                // Handle error
//                System.err.println(e.getMessage());
//            }
//
//            @Override
//            public void onSuccess(Permission permission,
//                                  HttpHeaders responseHeaders)
//                    throws IOException {
//                System.out.println("Permission ID: " + permission.getId());
//            }
//        };
//
//
//        try {
//            BatchRequest batch = googleDrive.batch();
//            Permission userPermission = new Permission()
//                    .setType("anyone")
//                    .setRole("writer");
//            googleDrive.permissions().create(fileId, userPermission)
//                    .setFields("id")
//                    .queue(batch, callback);
//
//            batch.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void printAbout() {
//        try {
//            About about = googleDrive.about().get().setFields("*").execute();
//            System.out.println("Total quota (bytes): " + about.getStorageQuota());
//            System.out.println("Max upload size (bytes): " + about.getMaxUploadSize());
//        } catch (IOException e) {
//            System.out.println("An error occurred: " + e);
//        }
//    }
//
//
//    @PostMapping("/uploadFile")
//    public ResponseEntity<?> uploadFile(@NotNull List<MultipartFile> files){
//        var result = files.stream().map(file -> {
//            try {
//                HttpServletRequest request = ((ServletRequestAttributes)
//                        Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
//                        .getRequest();
//                String s = request.getServletContext().getRealPath("");
//
//                System.out.println(file.getOriginalFilename());
//                var temp = new java.io.File(s + "/" +
//                        file.getOriginalFilename() + "-"
//                        + System.currentTimeMillis());
//
//
//                file.transferTo(temp);
//
//
//                var folderId = createNewFolder(file.getOriginalFilename());
//                var fileId = createNewFile(temp, file.getContentType(),
//                        file.getOriginalFilename(), Collections.singletonList(folderId));
//                setPublic(fileId);
//
//
//                if(!temp.delete()){
//                    System.out.println("delete fail");
//                }
//                return new DatabaseQueryResult(true, "okay",
//                        HttpStatus.OK, "https://drive.google.com/uc?export=view&id=" + fileId);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return new DatabaseQueryResult(false, "Error: " + e.getMessage(),
//                        HttpStatus.INTERNAL_SERVER_ERROR, "");
//            }
//        });
//        return ResponseEntity.ok(result);
//    }
}
