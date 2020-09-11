package com.app.manager.model.payload.response;

import com.app.manager.entity.Post;

import java.util.List;

public class PostResponse {
    private String id;
    private String content;
    private boolean pin;
    private String user_id;
    private UserProfileResponse userProfileResponse = new UserProfileResponse();
    private List<AttachmentResponse> attachmentResponses;
    private List<CommentResponse> commentResponses;
    private String session_id;
    private Post.StatusEnum status;
    private Long created_at;

    public PostResponse() {
    }

    public PostResponse(String id, String content, boolean pin,
                        String user_id, UserProfileResponse userProfileResponse,
                        List<AttachmentResponse> attachmentResponses,
                        List<CommentResponse> commentResponses, String session_id,
                        Post.StatusEnum status, Long created_at) {
        this.id = id;
        this.content = content;
        this.pin = pin;
        this.user_id = user_id;
        this.userProfileResponse = userProfileResponse;
        this.attachmentResponses = attachmentResponses;
        this.commentResponses = commentResponses;
        this.session_id = session_id;
        this.status = status;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public Post.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(Post.StatusEnum status) {
        this.status = status;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public UserProfileResponse getUserProfileResponse() {
        return userProfileResponse;
    }

    public void setUserProfileResponse(UserProfileResponse userProfileResponse) {
        this.userProfileResponse = userProfileResponse;
    }

    public List<AttachmentResponse> getAttachmentResponses() {
        return attachmentResponses;
    }

    public void setAttachmentResponses(List<AttachmentResponse> attachmentResponses) {
        this.attachmentResponses = attachmentResponses;
    }

    public List<CommentResponse> getCommentResponses() {
        return commentResponses;
    }

    public void setCommentResponses(List<CommentResponse> commentResponses) {
        this.commentResponses = commentResponses;
    }
}
