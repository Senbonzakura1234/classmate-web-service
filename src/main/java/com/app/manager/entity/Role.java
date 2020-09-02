package com.app.manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "[role]")
public class Role {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.SHOW;

    @JsonIgnore
    @Column(name = "created_at", nullable = false)
    private Long created_at = System.currentTimeMillis();

    @JsonIgnore
    @Column(name = "updated_at", nullable = false)
    private Long updated_at = System.currentTimeMillis();

    @JsonIgnore
    @Column(name = "deleted_at")
    private Long deleted_at;



    public Role(ERole name) {
        this.name = name;
    }

    public Role() {
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

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }

}
