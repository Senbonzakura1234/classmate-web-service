package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;

public class FaceCheckClientRequest {
    @NotBlank
    private String session_id;
    @NotBlank
    private String file_id;

    public FaceCheckClientRequest() {
    }

    public FaceCheckClientRequest(@NotBlank String session_id, @NotBlank String file_id) {
        this.session_id = session_id;
        this.file_id = file_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }
}
