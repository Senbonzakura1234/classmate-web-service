package com.app.manager.model.payload.response;

public class FaceCheckServerResponse {
    private String definition_id;

    private boolean matched;

    private double match_percent;

    public FaceCheckServerResponse() {
    }

    public FaceCheckServerResponse(String definition_id, boolean matched, double match_percent) {
        this.definition_id = definition_id;
        this.matched = matched;
        this.match_percent = match_percent;
    }

    public String getDefinition_id() {
        return definition_id;
    }

    public void setDefinition_id(String definition_id) {
        this.definition_id = definition_id;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public double getMatch_percent() {
        return match_percent;
    }

    public void setMatch_percent(double match_percent) {
        this.match_percent = match_percent;
    }
}
