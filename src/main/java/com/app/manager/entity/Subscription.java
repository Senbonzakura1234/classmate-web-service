package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Table(name = "[subscription]")
public class Subscription {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @OneToMany(mappedBy = "subscription")
    private List<User> users;

    @Column(name = "name", nullable = false)
    private String name;

    @Min(value = 0)
    @Column(name = "level", nullable = false)
    private int level;

    @Min(value = 0)
    @Column(name = "price", nullable = false)
    private double price;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "discount", nullable = false)
    private double discount;

    @Min(value = 0)
    @Column(name = "max_student", nullable = false)
    private int max_student;

    @Min(value = 0)
    @Column(name = "max_course_duration", nullable = false)
    private int max_course_duration;

    @Min(value = 0)
    @Column(name = "max_session_duration", nullable = false)
    private int max_session_duration;

    @Min(value = 0)
    @Column(name = "max_exercise_per_session", nullable = false)
    private int max_exercise_per_session;

    @Column(name = "allow_face_check", nullable = false)
    private boolean allow_face_check;


    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.SHOW;

    @Column(name = "created_at", nullable = false)
    private Long created_at = System.currentTimeMillis();

    @Column(name = "updated_at", nullable = false)
    private Long updated_at = System.currentTimeMillis();

    @Column(name = "deleted_at")
    private Long deleted_at;

    public Subscription() {
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

    public enum SubscriptionList {
        FREE(0, "FREE"),
        PREMIUM(1, "PREMIUM");

        private final int value;
        private final String name;

        SubscriptionList(int value, String name) {
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getMax_student() {
        return max_student;
    }

    public void setMax_student(int maxstudent) {
        this.max_student = maxstudent;
    }

    public int getMax_course_duration() {
        return max_course_duration;
    }

    public void setMax_course_duration(int maxcourseduration) {
        this.max_course_duration = maxcourseduration;
    }

    public int getMax_session_duration() {
        return max_session_duration;
    }

    public void setMax_session_duration(int maxsessionduration) {
        this.max_session_duration = maxsessionduration;
    }

    public int getMax_exercise_per_session() {
        return max_exercise_per_session;
    }

    public void setMax_exercise_per_session(int maxexercisepersession) {
        this.max_exercise_per_session = maxexercisepersession;
    }

    public boolean isAllow_face_check() {
        return allow_face_check;
    }

    public void setAllow_face_check(boolean allowfacecheck) {
        this.allow_face_check = allowfacecheck;
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
}
