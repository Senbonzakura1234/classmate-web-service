package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "exercise")
public class Exercise {
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

    @OneToMany(mappedBy = "exercise")
    private List<StudentExercise> studentExercises;



    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Min(value = 0)
    @Column(name = "duration", nullable = false)
    private int duration;




    @Column(name = "status", nullable = false)
    private StatusEnum status;

    @Column(name = "createdat", nullable = false)
    private Long createdat;

    @Column(name = "updatedat", nullable = false)
    private Long updatedat;

    @Column(name = "deletedat")
    private Long deletedat;

    public Exercise() {
        status = StatusEnum.ONGOING;
        createdat = System.currentTimeMillis();
        updatedat = System.currentTimeMillis();
    }

    public enum StatusEnum {
        ONGOING(0, "Ongoing"),
        MARKING(1, "Marking"),
        END(2, "End");

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

    public List<StudentExercise> getStudentExercises() {
        return studentExercises;
    }

    public void setStudentExercises(List<StudentExercise> studentExercises) {
        this.studentExercises = studentExercises;
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
}
