package com.app.manager.model.payload.response;

public class FaceCheckServerResponse {
    private String definition_id;

    private double match_percent;

    public FaceCheckServerResponse() {
    }

    public FaceCheckServerResponse(String definition_id, double match_percent) {
        this.definition_id = definition_id;
        this.match_percent = match_percent;
    }

    public String getDefinition_id() {
        return definition_id;
    }

    public void setDefinition_id(String definition_id) {
        this.definition_id = definition_id;
    }

    public double getMatch_percent() {
        return match_percent;
    }

    public void setMatch_percent(double match_percent) {
        this.match_percent = match_percent;
    }
}
