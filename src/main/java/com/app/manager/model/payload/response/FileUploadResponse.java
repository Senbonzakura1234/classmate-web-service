package com.app.manager.model.payload.response;

public class FileUploadResponse {
    private boolean upload_success = false;
    private String file_id = "";
    private String file_url = "";
    private String file_name = "";
    private Long file_size = 0L;

    public FileUploadResponse() {
    }

    public FileUploadResponse(boolean upload_success, String file_name) {
        this.upload_success = upload_success;
        this.file_name = file_name;
    }

    public FileUploadResponse(boolean upload_success, String file_id, String file_name, Long file_size) {
        this.upload_success = upload_success;
        this.file_id = file_id;
        this.file_url = "https://drive.google.com/uc?export=view&id=" + file_id;
        this.file_name = file_name;
        this.file_size = file_size;
    }

    public boolean isUpload_success() {
        return upload_success;
    }

    public void setUpload_success(boolean upload_success) {
        this.upload_success = upload_success;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public Long getFile_size() {
        return file_size;
    }

    public void setFile_size(Long file_size) {
        this.file_size = file_size;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }
}
