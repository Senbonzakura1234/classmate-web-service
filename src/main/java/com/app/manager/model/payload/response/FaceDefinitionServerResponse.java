package com.app.manager.model.payload.response;

public class FaceDefinitionServerResponse {
    private String definition_id;
    private boolean success;

    public FaceDefinitionServerResponse() {
    }

    public FaceDefinitionServerResponse(String definition_id, boolean success) {
        this.definition_id = definition_id;
        this.success = success;
    }

    public String getDefinition_id() {
        return definition_id;
    }

    public void setDefinition_id(String definition_id) {
        this.definition_id = definition_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
