package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "[session]")
public class Session {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "course_id")
    private String course_id;

    @ManyToOne
    @JoinColumn(name = "course_id", updatable = false, insertable = false)
    private Course course;


    @OneToMany(mappedBy = "session")
    private List<Attendance> attendances; 



    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "content", nullable = false, length = 512)
    private String content = "";


    @Column(name = "start_time", nullable = false)
    private Long start_time = 0L;

    @Min(value = 1)
    @Column(name = "session_duration", nullable = false)
    private int session_duration = 1;

    @Column(name = "attendance_check_start_time", nullable = false)
    private Long attendance_check_start_time = 0L;

    @Column(name = "attendance_status", nullable = false)
    private AttendanceStatusEnum attendance_status = AttendanceStatusEnum.PENDING;



    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.PENDING;

    @Column(name = "created_at", nullable = false)
    private Long created_at = System.currentTimeMillis();

    @Column(name = "updated_at", nullable = false)
    private Long updated_at = System.currentTimeMillis();

    @Column(name = "deleted_at")
    private Long deleted_at;

    public Session() {
    }

    public enum StatusEnum {
        ALL(0, "All"),
        PENDING(1, "Pending"),
        ONGOING(2, "Ongoing"),
//        ATTENDANCE_CHECK(3, "Attendance check"),
        END(4, "End"),
        CANCEL(5, "Cancel");

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

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String courseId) {
        this.course_id = courseId;
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

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long starttime) {
        this.start_time = starttime;
    }

    public int getSession_duration() {
        return session_duration;
    }

    public void setSession_duration(int attendanceduration) {
        this.session_duration = attendanceduration;
    }

    public AttendanceStatusEnum getAttendance_status() {
        return attendance_status;
    }

    public void setAttendance_status(AttendanceStatusEnum attendancechecked) {
        this.attendance_status = attendancechecked;
    }

    public Long getAttendance_check_start_time() {
        return attendance_check_start_time;
    }

    public void setAttendance_check_start_time(Long attendancecheckstarttime) {
        this.attendance_check_start_time = attendancecheckstarttime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
