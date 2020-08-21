package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "courseCategoryId")
    private String courseCategoryId;

    @ManyToOne
    @JoinColumn(name = "courseCategoryId", updatable = false, insertable = false)
    private CourseCategory courseCategory;

    @Column(name = "userId")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "userId", updatable = false, insertable = false)
    private User user;

    @OneToMany(mappedBy = "course")
    private List<Session> sessions;

    @OneToMany(mappedBy = "course")
    private List<StudentCourse> studentCourses;


    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "startdate", nullable = false)
    private Long startdate;

    @Column(name = "enddate", nullable = false)
    private Long enddate;



    @Column(name = "status", nullable = false)
    private StatusEnum status;

    @Column(name = "createdat", nullable = false)
    private Long createdat;

    @Column(name = "updatedat", nullable = false)
    private Long updatedat;

    @Column(name = "deletedat")
    private Long deletedat;

    public Course() {
        status = StatusEnum.PENDING;
        startdate = System.currentTimeMillis();
        enddate = System.currentTimeMillis();
        createdat = System.currentTimeMillis();
        updatedat = System.currentTimeMillis();
    }

    public enum StatusEnum {
        PENDING(0, "Pending"),
        ONGOING(1, "Ongoing"),
        CANCEL(2, "Cancel"),
        END(3, "End"),;

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

    public String getCourseCategoryId() {
        return courseCategoryId;
    }

    public void setCourseCategoryId(String courseCategoryId) {
        this.courseCategoryId = courseCategoryId;
    }

    public CourseCategory getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(CourseCategory courseCategory) {
        this.courseCategory = courseCategory;
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

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public List<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(List<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
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
