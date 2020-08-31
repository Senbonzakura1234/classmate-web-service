package com.app.manager.model.payload.response;

import com.app.manager.entity.EVisibility;
import com.app.manager.entity.File;

public class FileResponse {
    private String id;
    private String student_exercise_id;
    private String name;
    private String description;
    private String file_url;
    private Long file_size;
    private EVisibility file_visibility;
    private File.StatusEnum status;
    private Long created_at;

    public FileResponse() {
    }

    public FileResponse(String id, String student_exercise_id, String name,
                        String description, String file_url, Long file_size,
                        EVisibility file_visibility, File.StatusEnum status,
                        Long created_at) {
        this.id = id;
        this.student_exercise_id = student_exercise_id;
        this.name = name;
        this.description = description;
        this.file_url = file_url;
        this.file_size = file_size;
        this.file_visibility = file_visibility;
        this.status = status;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent_exercise_id() {
        return student_exercise_id;
    }

    public void setStudent_exercise_id(String student_exercise_id) {
        this.student_exercise_id = student_exercise_id;
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

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public Long getFile_size() {
        return file_size;
    }

    public void setFile_size(Long file_size) {
        this.file_size = file_size;
    }

    public EVisibility getFile_visibility() {
        return file_visibility;
    }

    public void setFile_visibility(EVisibility file_visibility) {
        this.file_visibility = file_visibility;
    }

    public File.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(File.StatusEnum status) {
        this.status = status;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }
}
