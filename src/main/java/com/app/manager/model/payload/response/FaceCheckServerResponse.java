package com.app.manager.model.payload.response;

public class FaceCheckServerResponse {
    private String definitionId;

    private boolean matched;

    private double match_percent;

    public FaceCheckServerResponse() {
    }

    public FaceCheckServerResponse(String definitionId, boolean matched, double match_percent) {
        this.definitionId = definitionId;
        this.matched = matched;
        this.match_percent = match_percent;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
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
