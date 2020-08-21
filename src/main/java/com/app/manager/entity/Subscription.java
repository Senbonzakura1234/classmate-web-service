package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Table(name = "subscription")
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
    @Column(name = "maxstudent", nullable = false)
    private int maxstudent;

    @Min(value = 0)
    @Column(name = "maxcourseduration", nullable = false)
    private int maxcourseduration;

    @Min(value = 0)
    @Column(name = "maxsessionduration", nullable = false)
    private int maxsessionduration;

    @Min(value = 0)
    @Column(name = "maxexercisepersession", nullable = false)
    private int maxexercisepersession;

    @Column(name = "allowfacecheck", nullable = false)
    private boolean allowfacecheck;


    @Column(name = "status", nullable = false)
    private StatusEnum status;

    @Column(name = "createdat", nullable = false)
    private Long createdat;

    @Column(name = "updatedat", nullable = false)
    private Long updatedat;

    @Column(name = "deletedat")
    private Long deletedat;

    public Subscription() {
        status = StatusEnum.SHOW;
        createdat = System.currentTimeMillis();
        updatedat = System.currentTimeMillis();
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

    public enum SubscriptionList {
        FREE(0, "FREE");

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

    public int getMaxstudent() {
        return maxstudent;
    }

    public void setMaxstudent(int maxstudent) {
        this.maxstudent = maxstudent;
    }

    public int getMaxcourseduration() {
        return maxcourseduration;
    }

    public void setMaxcourseduration(int maxcourseduration) {
        this.maxcourseduration = maxcourseduration;
    }

    public int getMaxsessionduration() {
        return maxsessionduration;
    }

    public void setMaxsessionduration(int maxsessionduration) {
        this.maxsessionduration = maxsessionduration;
    }

    public int getMaxexercisepersession() {
        return maxexercisepersession;
    }

    public void setMaxexercisepersession(int maxexercisepersession) {
        this.maxexercisepersession = maxexercisepersession;
    }

    public boolean isAllowfacecheck() {
        return allowfacecheck;
    }

    public void setAllowfacecheck(boolean allowfacecheck) {
        this.allowfacecheck = allowfacecheck;
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
}
