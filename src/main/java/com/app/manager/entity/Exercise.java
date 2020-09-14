package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "[exercise]")
public class Exercise {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "session_id")
    private String session_id;

    @ManyToOne
    @JoinColumn(name = "session_id", updatable = false, insertable = false)
    private Session session;

    @OneToMany(mappedBy = "exercise")
    private List<StudentExercise> student_exercises;



    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "show_answer", nullable = false)
    private boolean show_answer = false;


    @Column(name = "exercise_end_time", nullable = false)
    private Long exercise_end_time = 0L;


    @Column(name = "auto_start", nullable = false)
    private boolean auto_start = true;


    @Column(name = "auto_close", nullable = false)
    private boolean auto_close = true;


    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.PENDING;

    @Column(name = "created_at", nullable = false)
    private Long created_at = System.currentTimeMillis();

    @Column(name = "updated_at", nullable = false)
    private Long updated_at = System.currentTimeMillis();

    @Column(name = "deleted_at")
    private Long deleted_at;

    public Exercise() {
    }

    public enum StatusEnum {
        ALL(0, "All"),
        PENDING(0, "Pending"),
        ONGOING(1, "Ongoing"),
        MARKING(2, "Marking"),
        END(3, "End"),
        CANCEL(4, "Cancel");

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

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String sessionId) {
        this.session_id = sessionId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<StudentExercise> getStudent_exercises() {
        return student_exercises;
    }

    public void setStudent_exercises(List<StudentExercise> studentExercises) {
        this.student_exercises = studentExercises;
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

    public boolean isShow_answer() {
        return show_answer;
    }

    public void setShow_answer(boolean show_answer) {
        this.show_answer = show_answer;
    }

    public Long getExercise_end_time() {
        return exercise_end_time;
    }

    public void setExercise_end_time(Long exercise_start_time) {
        this.exercise_end_time = exercise_start_time;
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
}
