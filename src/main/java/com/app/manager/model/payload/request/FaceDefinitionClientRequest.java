package com.app.manager.model.payload.request;

import java.util.ArrayList;
import java.util.List;

public class FaceDefinitionClientRequest {

    private List<String> img_urls = new ArrayList<>();

    public FaceDefinitionClientRequest() {
    }

    public FaceDefinitionClientRequest(List<String> img_urls) {
        this.img_urls = img_urls;
    }

    public List<String> getImg_urls() {
        return img_urls;
    }

    public void setImg_urls(List<String> img_urls) {
        this.img_urls = img_urls;
    }
}
