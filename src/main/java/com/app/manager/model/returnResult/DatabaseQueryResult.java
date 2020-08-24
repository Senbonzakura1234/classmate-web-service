package com.app.manager.model.returnResult;


import org.springframework.http.HttpStatus;

public class DatabaseQueryResult {
    private boolean success;
    private String description;
    private HttpStatus HttpStatus;
    private Object content;

    public DatabaseQueryResult() {
    }

    public DatabaseQueryResult(boolean success, String description, HttpStatus httpStatus, Object content) {
        this.success = success;
        this.description = description;
        HttpStatus = httpStatus;
        this.content = content;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        HttpStatus = httpStatus;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
