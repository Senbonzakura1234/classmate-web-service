package com.app.manager.model.payload.request;

public class FaceCheckServerRequest {
    private String definition_id;

    private String file_id;

    public FaceCheckServerRequest() {
    }

    public FaceCheckServerRequest(String definition_id, String file_id) {
        this.definition_id = definition_id;
        this.file_id = file_id;
    }

    public String getDefinition_id() {
        return definition_id;
    }

    public void setDefinition_id(String definition_id) {
        this.definition_id = definition_id;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }
}
