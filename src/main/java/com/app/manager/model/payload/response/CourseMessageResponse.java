package com.app.manager.model.payload.response;

import com.app.manager.entity.Message;

import java.util.List;

public class CourseMessageResponse {
    private String id;
    private boolean pin;
    private String user_id;
    private UserProfileResponse userProfileResponse = new UserProfileResponse();
    private List<AttachmentResponse> attachmentResponses;
    private String session_id;
    private Message.StatusEnum status;
    private Long created_at;

    public CourseMessageResponse() {
    }

    public CourseMessageResponse(String id, boolean pin, String user_id,
                                 UserProfileResponse userProfileResponse,
                                 List<AttachmentResponse> attachmentResponses,
                                 String session_id, Message.StatusEnum status,
                                 Long created_at) {
        this.id = id;
        this.pin = pin;
        this.user_id = user_id;
        this.userProfileResponse = userProfileResponse;
        this.attachmentResponses = attachmentResponses;
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

    public Message.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(Message.StatusEnum status) {
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
}
