package com.app.manager.model.payload.response;

public class FaceDefinitionServerResponse {
    private String definitionId;
    private boolean success;

    public FaceDefinitionServerResponse() {
    }

    public FaceDefinitionServerResponse(String definitionId, boolean success) {
        this.definitionId = definitionId;
        this.success = success;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
