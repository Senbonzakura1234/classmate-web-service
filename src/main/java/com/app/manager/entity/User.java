package com.app.manager.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "subscriptionId")
    private String subscriptionId;

    @ManyToOne
    @JoinColumn(name = "subscriptionId", updatable = false, insertable = false)
    private Subscription subscription;

    @OneToMany(mappedBy = "user")
    private List<Session> sessions;

    @OneToMany(mappedBy = "user")
    private List<StudentCourse> studentCourses;

    @OneToMany(mappedBy = "user")
    private List<Course> courses;

    @OneToMany(mappedBy = "user")
    private List<Attendance> attendances;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @NotBlank()
    @Size(min = 6, max = 30)
    @Column(name = "username", nullable = false)
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$")
    @NotBlank()
    @Size(max = 200)
    @Column(name = "password", nullable = false)
    private String password;

    @Email()
    @NotBlank()
    @Size(max = 50)
    @Column(name = "email", nullable = false)
    private String email;





    @Column(name = "account_non_expired", nullable = false)
    private boolean account_non_expired;

    @Column(name = "account_non_locked", nullable = false)
    private boolean account_non_locked;

    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentials_non_expired;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;


    @Column(name = "createdat", nullable = false)
    private Long createdat;

    @Column(name = "updatedat", nullable = false)
    private Long updatedat;

    @Column(name = "deletedat")
    private Long deletedat;

    public User() {
        account_non_expired = true;
        account_non_locked = true;
        credentials_non_expired = true;
        enabled = true;

        createdat = System.currentTimeMillis();
        updatedat = System.currentTimeMillis();
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;

        account_non_expired = true;
        account_non_locked = true;
        credentials_non_expired = true;
        enabled = true;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAccount_non_expired() {
        return account_non_expired;
    }

    public void setAccount_non_expired(boolean account_non_expired) {
        this.account_non_expired = account_non_expired;
    }

    public boolean isAccount_non_locked() {
        return account_non_locked;
    }

    public void setAccount_non_locked(boolean account_non_locked) {
        this.account_non_locked = account_non_locked;
    }

    public boolean isCredentials_non_expired() {
        return credentials_non_expired;
    }

    public void setCredentials_non_expired(boolean credentials_non_expired) {
        this.credentials_non_expired = credentials_non_expired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
