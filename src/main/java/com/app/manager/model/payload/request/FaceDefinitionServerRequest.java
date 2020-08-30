package com.app.manager.model.payload.request;

import java.util.List;
import java.util.UUID;

public class FaceDefinitionServerRequest {
    private String definition_id = UUID.randomUUID().toString();

    private List<String> img_urls;

    private boolean update;

    private String oldDefinitionId;

    public FaceDefinitionServerRequest() {
    }

    public FaceDefinitionServerRequest(List<String> img_urls,
                                       boolean update,
                                       String oldDefinitionId) {
        this.img_urls = img_urls;
        this.update = update;
        this.oldDefinitionId = oldDefinitionId;
    }

    public String getDefinition_id() {
        return definition_id;
    }

    public void setDefinition_id(String definition_id) {
        this.definition_id = definition_id;
    }

    public List<String> getImg_urls() {
        return img_urls;
    }

    public void setImg_urls(List<String> img_urls) {
        this.img_urls = img_urls;
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
