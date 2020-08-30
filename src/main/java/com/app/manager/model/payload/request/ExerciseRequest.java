package com.app.manager.model.payload.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class ExerciseRequest {
    @NotBlank
    private String session_id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String answer;

    @Min(value = 0)
    private int duration;

    private boolean show_answer = false;


    public ExerciseRequest() {
    }

    public ExerciseRequest(@NotBlank String session_id, @NotBlank String title,
                           @NotBlank String content, String answer,
                           @Min(value = 0) int duration, boolean show_answer) {
        this.session_id = session_id;
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.duration = duration;
        this.show_answer = show_answer;
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
}
