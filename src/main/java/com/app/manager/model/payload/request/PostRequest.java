package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class PostRequest {
    @NotBlank
    private String content;

    private List<AttachmentRequest> attachmentRequests = new ArrayList<>();

    public PostRequest() {
    }

    public PostRequest(@NotBlank String content,
                       List<AttachmentRequest> attachmentRequests) {
        this.content = content;
        this.attachmentRequests = attachmentRequests;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AttachmentRequest> getAttachmentRequests() {
        return attachmentRequests;
    }

    public void setAttachmentRequests(List<AttachmentRequest> attachmentRequests) {
        this.attachmentRequests = attachmentRequests;
    }
}
