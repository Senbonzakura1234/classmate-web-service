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

    @Column(name = "coursecategoryid")
    private String coursecategoryid;

    @ManyToOne
    @JoinColumn(name = "coursecategoryid", updatable = false, insertable = false)
    private CourseCategory coursecategory;

    @Column(name = "userid")
    private String userid;

    @ManyToOne
    @JoinColumn(name = "userid", updatable = false, insertable = false)
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
    private Long startdate = System.currentTimeMillis();

    @Column(name = "enddate", nullable = false)
    private Long enddate = System.currentTimeMillis();

    public Long getStartdate() {
        return startdate;
    }

    public void setStartdate(Long startdate) {
        this.startdate = startdate;
    }

    public Long getEnddate() {
        return enddate;
    }

    public void setEnddate(Long enddate) {
        this.enddate = enddate;
    }

    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.PENDING;

    @Column(name = "createdat", nullable = false)
    private Long createdat = System.currentTimeMillis();

    @Column(name = "updatedat", nullable = false)
    private Long updatedat = System.currentTimeMillis();

    @Column(name = "deletedat")
    private Long deletedat;


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

    public String getCoursecategoryid() {
        return coursecategoryid;
    }

    public void setCoursecategoryid(String courseCategoryId) {
        this.coursecategoryid = courseCategoryId;
    }

    public CourseCategory getCoursecategory() {
        return coursecategory;
    }

    public void setCoursecategory(CourseCategory courseCategory) {
        this.coursecategory = courseCategory;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userId) {
        this.userid = userId;
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
