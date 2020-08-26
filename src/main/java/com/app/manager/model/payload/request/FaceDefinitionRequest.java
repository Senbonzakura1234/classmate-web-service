package com.app.manager.model.payload.request;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FaceDefinitionRequest {

    private String definitionId =  UUID.randomUUID().toString();

    private List<String> img_urls = new ArrayList<>();

    public FaceDefinitionRequest() {
    }

    public FaceDefinitionRequest(List<String> img_urls) {
        this.img_urls = img_urls;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    public List<String> getImg_urls() {
        return img_urls;
    }

    public void setImg_urls(List<String> img_urls) {
        this.img_urls = img_urls;
    }
}
