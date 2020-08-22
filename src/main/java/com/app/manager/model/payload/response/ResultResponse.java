package com.app.manager.model.payload.response;

public class ResultResponse {
    private String name;
    private double percent;
    private boolean valid;

    public ResultResponse() {
    }

    public ResultResponse(String name, double percent, boolean valid) {
        this.name = name;
        this.percent = percent;
        this.valid = valid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
