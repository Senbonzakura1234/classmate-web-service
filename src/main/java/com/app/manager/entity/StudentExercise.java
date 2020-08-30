package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "studentexercise")
public class StudentExercise {
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

    @Column(name = "exercise_id")
    private String exercise_id;

    @ManyToOne
    @JoinColumn(name = "exercise_id", updatable = false, insertable = false)
    private Exercise exercise;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "message", nullable = false)
    private String message;



    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.SHOW;

    @Column(name = "created_at", nullable = false)
    private Long created_at = System.currentTimeMillis();

    @Column(name = "updated_at", nullable = false)
    private Long updated_at = System.currentTimeMillis();

    @Column(name = "deleted_at")
    private Long deleted_at;

    public StudentExercise() {
    }

    public enum StatusEnum {
        ALL(0, "All"),
        SHOW(1, "Show"),
        HIDE(2, "Hide");

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

    public void setCreated_at(Long createdat) {
        this.created_at = createdat;
    }

    public Long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Long updatedat) {
        this.updated_at = updatedat;
    }

    public Long getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Long deletedat) {
        this.deleted_at = deletedat;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String userId) {
        this.user_id = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(String exerciseId) {
        this.exercise_id = exerciseId;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
