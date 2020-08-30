package com.app.manager.model.payload.request;

public class FaceCheckServerRequest {
    private String definition_id;

    private String imgUrl;

    public FaceCheckServerRequest() {
    }

    public FaceCheckServerRequest(String definition_id, String imgUrl) {
        this.definition_id = definition_id;
        this.imgUrl = imgUrl;
    }

    public String getDefinition_id() {
        return definition_id;
    }

    public void setDefinition_id(String definition_id) {
        this.definition_id = definition_id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
