package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "[post]")
public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "user_id")
    private String user_id;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @Column(name = "course_id")
    private String course_id;

    @ManyToOne
    @JoinColumn(name = "course_id", updatable = false, insertable = false)
    private Course course;


    @OneToMany(mappedBy = "post")
    private List<Attachment> attachments;


    @OneToMany(mappedBy = "post")
    private List<Comment> comments;



    @NotBlank
    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @Column(name = "pin", nullable = false)
    private boolean pin = false;




    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.SHOW;

    @Column(name = "created_at", nullable = false)
    private Long created_at = System.currentTimeMillis();

    @Column(name = "updated_at", nullable = false)
    private Long updated_at = System.currentTimeMillis();

    @Column(name = "deleted_at")
    private Long deleted_at;

    public Post() {
    }

    public enum StatusEnum {
        SHOW(0, "Show"),
        HIDE(1, "Hide");

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

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public Long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Long updated_at) {
        this.updated_at = updated_at;
    }

    public Long getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Long deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String session_id) {
        this.course_id = session_id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course session) {
        this.course = session;
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

    public void setPin(boolean pinned) {
        this.pin = pinned;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
