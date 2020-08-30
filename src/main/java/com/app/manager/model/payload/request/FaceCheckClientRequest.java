package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;

public class FaceCheckClientRequest {
    @NotBlank
    private String session_id;
    @NotBlank
    private String img_url;

    public FaceCheckClientRequest() {
    }

    public FaceCheckClientRequest(@NotBlank String session_id, @NotBlank String img_url) {
        this.session_id = session_id;
        this.img_url = img_url;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
