package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;

public class ImageLinkRequest {
    @NotBlank
    private String img_uri;

    public ImageLinkRequest() {
    }

    public ImageLinkRequest(String img_uri) {
        this.img_uri = img_uri;
    }

    public String getImg_uri() {
        return img_uri;
    }

    public void setImg_uri(String img_uri) {
        this.img_uri = img_uri;
    }
}
