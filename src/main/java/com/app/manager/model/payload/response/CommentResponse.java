package com.app.manager.model.payload.response;

import com.app.manager.entity.Comment;

public class CommentResponse {
    private String user_id;
    private UserProfileResponse userProfileResponse;
    private String post_id;
    private String content;
    private boolean pin;
    private Comment.StatusEnum status;
    private Long created_at;

    public CommentResponse() {
    }

    public CommentResponse(String user_id, UserProfileResponse userProfileResponse,
                           String post_id, String content, boolean pin,
                           Comment.StatusEnum status, Long created_at) {
        this.user_id = user_id;
        this.userProfileResponse = userProfileResponse;
        this.post_id = post_id;
        this.content = content;
        this.pin = pin;
        this.status = status;
        this.created_at = created_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public UserProfileResponse getUserProfileResponse() {
        return userProfileResponse;
    }

    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        this.userProfileResponse = userProfileResponse;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPin() {
        return pin;
    }

    public void setPin(boolean pin) {
        this.pin = pin;
    }

    public Comment.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(Comment.StatusEnum status) {
        this.status = status;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }
}
