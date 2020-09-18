package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "[file]")
public class File {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "studentexercise_id")
    private String studentexercise_id;

    @ManyToOne
    @JoinColumn(name = "studentexercise_id", updatable = false, insertable = false)
    private StudentExercise studentexercise;




    @Column(name = "name", nullable = false, length = 2000)
    private String name;

    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @NotBlank
    @Column(name = "file_id", nullable = false)
    private String file_id;

    @Min(0L)
    @NotNull
    @Column(name = "file_size", nullable = false)
    private Long file_size = 0L;

    @Column(name = "file_visibility", nullable = false)
    private EVisibility file_visibility = EVisibility.TEACHERCOURSE;




    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.SHOW;

    @Column(name = "created_at", nullable = false)
    private Long created_at = System.currentTimeMillis();

    @Column(name = "updated_at", nullable = false)
    private Long updated_at = System.currentTimeMillis();

    @Column(name = "deleted_at")
    private Long deleted_at;

    public File() {
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

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public Long getFile_size() {
        return file_size;
    }

    public void setFile_size(Long file_size) {
        this.file_size = file_size;
    }

    public EVisibility getFile_visibility() {
        return file_visibility;
    }

    public void setFile_visibility(EVisibility file_visibility) {
        this.file_visibility = file_visibility;
    }

    public String getStudentexercise_id() {
        return studentexercise_id;
    }

    public void setStudentexercise_id(String student_exercise_id) {
        this.studentexercise_id = student_exercise_id;
    }

    public StudentExercise getStudentexercise() {
        return studentexercise;
    }

    public void setStudentexercise(StudentExercise student_exercise) {
        this.studentexercise = student_exercise;
    }
}
