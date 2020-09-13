package com.app.manager.model.payload.request;

import java.util.ArrayList;
import java.util.List;

public class FaceDefinitionClientRequest {

    private List<String> file_ids = new ArrayList<>();

    public FaceDefinitionClientRequest() {
    }

    public FaceDefinitionClientRequest(List<String> file_ids) {
        this.file_ids = file_ids;
    }

    public List<String> getFile_ids() {
        return file_ids;
    }

    public void setFile_ids(List<String> file_ids) {
        this.file_ids = file_ids;
    }
}
