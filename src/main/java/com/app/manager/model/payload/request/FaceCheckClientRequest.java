package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;

public class FaceCheckClientRequest {
    @NotBlank
    private String sessionid;
    @NotBlank
    private String img_url;

    public FaceCheckClientRequest() {
    }

    public FaceCheckClientRequest(@NotBlank String sessionid, @NotBlank String img_url) {
        this.sessionid = sessionid;
        this.img_url = img_url;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
