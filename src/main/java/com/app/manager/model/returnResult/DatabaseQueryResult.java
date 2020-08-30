package com.app.manager.model.returnResult;


import org.springframework.http.HttpStatus;

public class DatabaseQueryResult {
    private boolean success;
    private String description;
    private HttpStatus http_status;
    private Object content;

    public DatabaseQueryResult() {
    }

    public DatabaseQueryResult(boolean success, String description, HttpStatus httpstatus, Object content) {
        this.success = success;
        this.description = description;
        http_status = httpstatus;
        this.content = content;
    }

    public HttpStatus getHttp_status() {
        return http_status;
    }

    public void setHttp_status(HttpStatus http_status) {
        this.http_status = http_status;
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
