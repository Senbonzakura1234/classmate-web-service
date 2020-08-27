package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "session")
public class Session {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "courseid")
    private String courseid;

    @ManyToOne
    @JoinColumn(name = "courseid", updatable = false, insertable = false)
    private Course course;


    @OneToMany(mappedBy = "session")
    private List<Attendance> attendances; 



    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "starttime", nullable = false)
    private Long starttime;

    @Min(value = 1)
    @Column(name = "sessionduration", nullable = false)
    private int sessionduration = 1;

    @Column(name = "attendancecheckstarttime", nullable = false)
    private Long attendancecheckstarttime = 0L;

    @Column(name = "attendancestatus", nullable = false)
    private AttendanceStatusEnum attendancestatus = AttendanceStatusEnum.PENDING;



    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.PENDING;

    @Column(name = "createdat", nullable = false)
    private Long createdat = System.currentTimeMillis();

    @Column(name = "updatedat", nullable = false)
    private Long updatedat = System.currentTimeMillis();

    @Column(name = "deletedat")
    private Long deletedat;

    public Session() {
    }

    public enum StatusEnum {
        ALL(0, "All"),
        PENDING(1, "Pending"),
        ONGOING(2, "Ongoing"),
//        ATTENDANCE_CHECK(3, "Attendance check"),
        EXERCISING(3, "Exercising"),
        END(4, "END");

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

    public enum AttendanceStatusEnum {
        PENDING(1, "Pending"),
        ONGOING(2, "Ongoing"),
        END(3, "END");

        private final int value;
        private final String name;

        AttendanceStatusEnum(int value, String name) {
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

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseId) {
        this.courseid = courseId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStarttime() {
        return starttime;
    }

    public void setStarttime(Long starttime) {
        this.starttime = starttime;
    }

    public int getSessionduration() {
        return sessionduration;
    }

    public void setSessionduration(int attendanceduration) {
        this.sessionduration = attendanceduration;
    }

    public AttendanceStatusEnum getAttendancestatus() {
        return attendancestatus;
    }

    public void setAttendancestatus(AttendanceStatusEnum attendancechecked) {
        this.attendancestatus = attendancechecked;
    }

    public Long getAttendancecheckstarttime() {
        return attendancecheckstarttime;
    }

    public void setAttendancecheckstarttime(Long attendancecheckstarttime) {
        this.attendancecheckstarttime = attendancecheckstarttime;
    }
}
