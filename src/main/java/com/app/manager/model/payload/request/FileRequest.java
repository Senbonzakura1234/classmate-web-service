package com.app.manager.model.payload.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class FileRequest {
    private String name = "";
    private String description = "";
    @NotBlank
    private String file_url;
    @Min(0L)
    @NotNull
    private Long file_size = 0L;

    public FileRequest() {
    }

    public FileRequest(String name, String description,
                       @NotBlank String file_url,
                       @Min(0L) @NotNull Long file_size) {
        this.name = name;
        this.description = description;
        this.file_url = file_url;
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
}
