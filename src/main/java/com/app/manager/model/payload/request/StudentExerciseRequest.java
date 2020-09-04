package com.app.manager.model.payload.request;

import java.util.ArrayList;
import java.util.List;

public class StudentExerciseRequest {
    private String content = "";
    private String student_message = "";

    private List<FileRequest> fileRequests = new ArrayList<>();

    public StudentExerciseRequest() {
    }

    public StudentExerciseRequest(String content, String student_message,
                                  List<FileRequest> fileRequests) {
        this.content = content;
        this.student_message = student_message;
        this.fileRequests = fileRequests;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStudent_message() {
        return student_message;
    }

    public void setStudent_message(String student_message) {
        this.student_message = student_message;
    }

    public List<FileRequest> getFileRequests() {
        return fileRequests;
    }

    public void setFileRequests(List<FileRequest> fileRequests) {
        this.fileRequests = fileRequests;
    }
}
