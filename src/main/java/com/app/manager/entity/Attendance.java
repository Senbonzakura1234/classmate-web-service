package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "userId")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "userId", updatable = false, insertable = false)
    private User user;

    @Column(name = "sessionId")
    private String sessionId;

    @ManyToOne
    @JoinColumn(name = "sessionId", updatable = false, insertable = false)
    private Session session;



    @Column(name = "face_matched", nullable = false)
    private boolean face_matched = false;

    @Column(name = "image_uri", nullable = false)
    private String image_uri = "";


    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.ALL;

    @Column(name = "createdat", nullable = false)
    private Long createdat = System.currentTimeMillis();

    @Column(name = "updatedat", nullable = false)
    private Long updatedat = System.currentTimeMillis();

    @Column(name = "deletedat")
    private Long deletedat;

    public Attendance() {
    }

    public enum StatusEnum {
        ALL(0, "All"),
        ATTENDANT(1, "On time"),
        ABSENT(2, "Absent");

        private final int value;
        private final String name;

        StatusEnum(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Long getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Long createdat) {
        this.createdat = createdat;
    }

    public Long getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Long updatedat) {
        this.updatedat = updatedat;
    }

    public Long getDeletedat() {
        return deletedat;
    }

    public void setDeletedat(Long deletedat) {
        this.deletedat = deletedat;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public boolean isFace_matched() {
        return face_matched;
    }

    public void setFace_matched(boolean face_matched) {
        this.face_matched = face_matched;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }
}
