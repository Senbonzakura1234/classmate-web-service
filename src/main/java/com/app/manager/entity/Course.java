package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "course")
public class Course implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "course_category_id")
    private String course_category_id;

    @ManyToOne
    @JoinColumn(name = "course_category_id", updatable = false, insertable = false)
    private CourseCategory course_category;

    @Column(name = "user_id")
    private String user_id;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @OneToMany(mappedBy = "course")
    private List<Session> sessions;

    @OneToMany(mappedBy = "course")
    private List<StudentCourse> student_courses;


    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private Long start_date = System.currentTimeMillis();

    @Column(name = "end_date", nullable = false)
    private Long end_date = System.currentTimeMillis();

    public Long getStart_date() {
        return start_date;
    }

    public void setStart_date(Long startdate) {
        this.start_date = startdate;
    }

    public Long getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Long enddate) {
        this.end_date = enddate;
    }

    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.PENDING;

    @Column(name = "create_dat", nullable = false)
    private Long created_at = System.currentTimeMillis();

    @Column(name = "updated_at", nullable = false)
    private Long updated_at = System.currentTimeMillis();

    @Column(name = "deleted_at")
    private Long deleted_at;


    public Course() {
    }

    public enum StatusEnum {
        ALL(0, "All"),
        PENDING(1, "Pending"),
        ONGOING(2, "Ongoing"),
        CANCEL(3, "Cancel"),
        END(4, "End"),;

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

    public String getCourse_category_id() {
        return course_category_id;
    }

    public void setCourse_category_id(String courseCategoryId) {
        this.course_category_id = courseCategoryId;
    }

    public CourseCategory getCourse_category() {
        return course_category;
    }

    public void setCourse_category(CourseCategory courseCategory) {
        this.course_category = courseCategory;
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

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public List<StudentCourse> getStudent_courses() {
        return student_courses;
    }

    public void setStudent_courses(List<StudentCourse> studentCourses) {
        this.student_courses = studentCourses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
