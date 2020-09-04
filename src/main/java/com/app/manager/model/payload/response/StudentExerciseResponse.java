package com.app.manager.model.payload.response;

import com.app.manager.entity.StudentExercise;

import java.util.List;

public class StudentExerciseResponse {
    private String id;
    private String user_id;
    private String exercise_id;
    private String content;
    private String student_message;
    private StudentExercise.StatusEnum status;
    private Long created_at;
    private double mark;
    private boolean marked;
    private String teacher_message;

    private List<FileResponse> fileResponses;

    public StudentExerciseResponse() {
    }

    public StudentExerciseResponse(String id, String user_id,
                                   String exercise_id, String content,
                                   String student_message, StudentExercise.StatusEnum status,
                                   Long created_at, List<FileResponse> fileResponses) {
        this.id = id;
        this.user_id = user_id;
        this.exercise_id = exercise_id;
        this.content = content;
        this.student_message = student_message;
        this.status = status;
        this.created_at = created_at;
        this.fileResponses = fileResponses;
    }

    public StudentExerciseResponse(String id, String user_id, String exercise_id,
                                   String content, String student_message,
                                   StudentExercise.StatusEnum status, Long created_at,
                                   double mark, boolean marked, String teacher_message,
                                   List<FileResponse> fileResponses) {
        this.id = id;
        this.user_id = user_id;
        this.exercise_id = exercise_id;
        this.content = content;
        this.student_message = student_message;
        this.status = status;
        this.created_at = created_at;
        this.mark = mark;
        this.marked = marked;
        this.teacher_message = teacher_message;
        this.fileResponses = fileResponses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(String exercise_id) {
        this.exercise_id = exercise_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStudent_message() {
        return student_message;
    }

    public void setStudent_message(String student_message) {
        this.student_message = student_message;
    }

    public StudentExercise.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StudentExercise.StatusEnum status) {
        this.status = status;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public List<FileResponse> getFileResponses() {
        return fileResponses;
    }

    public void setFileResponses(List<FileResponse> fileResponses) {
        this.fileResponses = fileResponses;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public String getTeacher_message() {
        return teacher_message;
    }

    public void setTeacher_message(String teacher_message) {
        this.teacher_message = teacher_message;
    }
}
