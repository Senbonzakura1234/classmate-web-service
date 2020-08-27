package com.app.manager.model.payload.request;

public class FaceCheckServerRequest {
    private String definitionId;

    private String imgUrl;

    public FaceCheckServerRequest() {
    }

    public FaceCheckServerRequest(String definitionId, String imgUrl) {
        this.definitionId = definitionId;
        this.imgUrl = imgUrl;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
