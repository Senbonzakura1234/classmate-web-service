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
@Table(name = "[user]", uniqueConstraints = {
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

    @Column(name = "subscription_id")
    private String subscription_id;

    @ManyToOne
    @JoinColumn(name = "subscription_id", updatable = false, insertable = false)
    private Subscription subscription;

    @OneToMany(mappedBy = "user")
    private List<StudentCourse> student_courses;

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


    @Column(name = "fullname", nullable = false)
    private String fullname = "";

    @Column(name = "phone", nullable = false)
    private String phone = "";

    @Column(name = "address", nullable = false)
    private String address = "";

    @Column(name = "civil_id", nullable = false)
    private String civil_id = "";

    @Column(name = "birthday")
    private Long birthday = 0L;

    @Column(name = "gender", nullable = false)
    private GenderEnum gender = GenderEnum.UNKNOWN;

    @Column(name = "profile_visibility", nullable = false)
    private VisibilityEnum profile_visibility = VisibilityEnum.PRIVATE;



    @Column(name = "face_definition_id", nullable = false)
    private String face_definition_id = "";

    @Column(name = "facedefinition", nullable = false)
    private boolean face_definition = false;



    @Column(name = "account_non_expired", nullable = false)
    private boolean account_non_expired = true;

    @Column(name = "account_non_locked", nullable = false)
    private boolean account_non_locked = true;

    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentials_non_expired = true;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;


    @Column(name = "created_at", nullable = false)
    private Long created_at = System.currentTimeMillis();

    @Column(name = "updated_at", nullable = false)
    private Long updated_at = System.currentTimeMillis();

    @Column(name = "deleted_at")
    private Long deleted_at;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public enum GenderEnum {
        ALL(0, "All"),
        MALE(1, "Male"),
        FEMALE(2, "Female"),
        OTHER(3, "Other"),
        UNKNOWN(3, "Unknown");

        private final int value;
        private final String name;

        GenderEnum(int value, String name) {
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


    public enum VisibilityEnum {
        PUBLIC(0, "Public"),
        COURSE(1, "Classmate and teacher only"),
        TEACHER(2, "Teacher only"),
        PRIVATE(3, "Private");

        private final int value;
        private final String name;

        VisibilityEnum(int value, String name) {
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

    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscriptionId) {
        this.subscription_id = subscriptionId;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public List<StudentCourse> getStudent_courses() {
        return student_courses;
    }

    public void setStudent_courses(List<StudentCourse> studentCourses) {
        this.student_courses = studentCourses;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCivil_id() {
        return civil_id;
    }

    public void setCivil_id(String civil_id) {
        this.civil_id = civil_id;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public VisibilityEnum getProfile_visibility() {
        return profile_visibility;
    }

    public void setProfile_visibility(VisibilityEnum profile_visibility) {
        this.profile_visibility = profile_visibility;
    }

    public String getFace_definition_id() {
        return face_definition_id;
    }

    public void setFace_definition_id(String facedefinitionid) {
        this.face_definition_id = facedefinitionid;
    }

    public boolean isFace_definition() {
        return face_definition;
    }

    public void setFace_definition(boolean facedefinition) {
        this.face_definition = facedefinition;
    }
}
