package com.app.manager.model.payload.response;

public class MessageResponse {
    private String message;
    private Object detail;

    public MessageResponse() {
    }

    public MessageResponse(String message, Object detail) {
        this.message = message;
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }
}
