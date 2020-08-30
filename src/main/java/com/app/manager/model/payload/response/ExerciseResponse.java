package com.app.manager.model.payload.response;

import com.app.manager.entity.Exercise;

public class ExerciseResponse {
    private String id;
    private String session_id;
    private String title;
    private String content;
    private String answer;
    private int duration;
    private boolean show_answer;
    private Exercise.StatusEnum status;
    private Long created_at;

    public ExerciseResponse() {
    }

    public ExerciseResponse(String id, String session_id, String title,
                            String content, String answer, int duration,
                            boolean show_answer, Exercise.StatusEnum status,
                            Long created_at) {
        this.id = id;
        this.session_id = session_id;
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.duration = duration;
        this.show_answer = show_answer;
        this.status = status;
        this.created_at = created_at;
    }

    public static ExerciseResponse castToObjectModel(Exercise exercise){
        if(exercise == null) return new ExerciseResponse();
        return new ExerciseResponse(exercise.getId(), exercise.getSession_id(),
                exercise.getTitle(), exercise.getContent(),
                exercise.isShow_answer() ? exercise.getAnswer() : "",
                exercise.getDuration(), exercise.isShow_answer(),
                exercise.getStatus(), exercise.getCreated_at());
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
}
