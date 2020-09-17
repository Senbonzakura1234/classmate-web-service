package com.app.manager.model.payload.response;

import com.app.manager.entity.Exercise;

import java.util.List;

public class ExerciseResponse {
    private String id;
    private String session_id;
    private String title;
    private String content;
    private String answer;
    private Long exercise_end_time;
    private boolean show_answer;
    private boolean auto_start;
    private boolean auto_close;
    private Exercise.StatusEnum status;
    private Long created_at;
    private List<GradeRecordResponse> gradeRecordResponses;

    public ExerciseResponse() {
    }


    public ExerciseResponse(String id, String session_id, String title,
                            String content, String answer, Long exercise_end_time,
                            boolean show_answer, boolean auto_start, boolean auto_close,
                            Exercise.StatusEnum status, Long created_at,
                            List<GradeRecordResponse> gradeRecordResponses) {
        this.id = id;
        this.session_id = session_id;
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.exercise_end_time = exercise_end_time;
        this.show_answer = show_answer;
        this.auto_start = auto_start;
        this.auto_close = auto_close;
        this.status = status;
        this.created_at = created_at;
        this.gradeRecordResponses = gradeRecordResponses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getExercise_end_time() {
        return exercise_end_time;
    }

    public void setExercise_end_time(Long exercise_end_time) {
        this.exercise_end_time = exercise_end_time;
    }

    public boolean isShow_answer() {
        return show_answer;
    }

    public void setShow_answer(boolean show_answer) {
        this.show_answer = show_answer;
    }

    public Exercise.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(Exercise.StatusEnum status) {
        this.status = status;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public boolean isAuto_start() {
        return auto_start;
    }

    public void setAuto_start(boolean auto_start) {
        this.auto_start = auto_start;
    }

    public boolean isAuto_close() {
        return auto_close;
    }

    public void setAuto_close(boolean auto_close) {
        this.auto_close = auto_close;
    }

    public List<GradeRecordResponse> getGradeRecordResponses() {
        return gradeRecordResponses;
    }

    public void setGradeRecordResponses(List<GradeRecordResponse> gradeRecordResponses) {
        this.gradeRecordResponses = gradeRecordResponses;
    }
}
