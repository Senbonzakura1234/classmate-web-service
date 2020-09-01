package com.app.manager.model.payload.request;

import java.util.ArrayList;
import java.util.List;

public class StudentExerciseRequest {
    private String content = "";
    private String message = "";

    private List<FileRequest> fileRequests = new ArrayList<>();

    public StudentExerciseRequest() {
    }

    public StudentExerciseRequest(String content, String message,
                                  List<FileRequest> fileRequests) {
        this.content = content;
        this.message = message;
        this.fileRequests = fileRequests;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FileRequest> getFileRequests() {
        return fileRequests;
    }

    public void setFileRequests(List<FileRequest> fileRequests) {
        this.fileRequests = fileRequests;
    }
}
