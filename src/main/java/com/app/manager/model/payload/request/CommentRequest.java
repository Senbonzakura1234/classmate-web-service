package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;

public class CommentRequest {
    @NotBlank
    private String content;

    public CommentRequest() {
    }

    public CommentRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
