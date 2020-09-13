package com.app.manager.model.payload.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AttachmentRequest {
    private String name = "";
    private String description = "";
    @NotBlank
    private String file_id;
    @Min(0L)
    @NotNull
    private Long file_size = 0L;

    public AttachmentRequest() {
    }

    public AttachmentRequest(String name, String description,
                             @NotBlank String file_id,
                             @Min(0L) @NotNull Long file_size) {
        this.name = name;
        this.description = description;
        this.file_id = file_id;
        this.file_size = file_size;
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

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public Long getFile_size() {
        return file_size;
    }

    public void setFile_size(Long file_size) {
        this.file_size = file_size;
    }
}
