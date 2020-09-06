package com.app.manager.model.payload.response;

import com.app.manager.entity.Attachment;

public class AttachmentResponse {
    private String id;
    private String message_id;
    private String name;
    private String description;
    private String file_url;
    private Long file_size;
    private Attachment.StatusEnum status;
    private Long created_at;

    public AttachmentResponse() {
    }

    public AttachmentResponse(String id, String message_id, String name,
                              String description, String file_url, Long file_size,
                              Attachment.StatusEnum status, Long created_at) {
        this.id = id;
        this.message_id = message_id;
        this.name = name;
        this.description = description;
        this.file_url = file_url;
        this.file_size = file_size;
        this.status = status;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public Long getFile_size() {
        return file_size;
    }

    public void setFile_size(Long file_size) {
        this.file_size = file_size;
    }

    public Attachment.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(Attachment.StatusEnum status) {
        this.status = status;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }
}
