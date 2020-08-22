package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;

public class ImageLinkRequest {
    @NotBlank
    private String imgUri;

    public ImageLinkRequest() {
    }

    public ImageLinkRequest(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }
}
