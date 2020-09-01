package com.app.manager.model.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CourseCategoryRequest {
    @NotBlank
    private String name;
    @NotNull
    private String description;

    public CourseCategoryRequest() {
    }

    public CourseCategoryRequest(@NotBlank String name,
                                 @NotNull String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
