package com.app.manager.model.payload.request;

import java.util.List;
import java.util.UUID;

public class FaceDefinitionServerRequest {
    private String definition_id = UUID.randomUUID().toString();

    private List<String> file_ids;

    private boolean update;

    private String oldDefinitionId;

    private String user_id;

    public FaceDefinitionServerRequest() {
    }

    public FaceDefinitionServerRequest(List<String> file_ids,
                                       boolean update, String oldDefinitionId,
                                       String user_id) {
        this.file_ids = file_ids;
        this.update = update;
        this.oldDefinitionId = oldDefinitionId;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDefinition_id() {
        return definition_id;
    }

    public void setDefinition_id(String definition_id) {
        this.definition_id = definition_id;
    }

    public List<String> getFile_ids() {
        return file_ids;
    }

    public void setFile_ids(List<String> file_ids) {
        this.file_ids = file_ids;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getOldDefinitionId() {
        return oldDefinitionId;
    }

    public void setOldDefinitionId(String oldDefinitionId) {
        this.oldDefinitionId = oldDefinitionId;
    }
}
