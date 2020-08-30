package com.app.manager.model.payload.request;

import com.app.manager.model.HelperMethod;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class SignupRequest {
    @NotBlank(message = "Please provide username")
    @Size(min = 6, max = 30, message = "Username has at least 6 and max 30 characters")
    private String username;

    @Email(message = "Please provide valid email")
    @NotBlank(message = "Please provide your email")
    @Size(max = 50, message = "Email too long")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
            message = "Password must has an uppercase, an lower case and a numeric character")
    @NotBlank(message = "Please provide password")
    @Size(max = 200, message = "Password too long")
    private String password;


    private Set<String> role;

    public SignupRequest() {
        role = new HashSet<>();
    }

//    public SignupRequest(@NotBlank(message = "Please provide username")
//                         @Size(min = 6, max = 30,
//                             message = "Username has at least 6 and max 30 characters")
//                             String username,
//                         @Email(message = "Please provide valid email")
//                         @NotBlank(message = "Please provide your email")
//                         @Size(max = 50, message = "Email too long")
//                             String email,
//                         @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
//                             message = "Password must has an uppercase, " +
//                                "an lower case and a numeric character")
//                         @NotBlank(message = "Please provide password")
//                         @Size(max = 200, message = "Password too long")
//                             String password) {
//        role = new HashSet<>();
//        role.add("Admin");
//        this.username = username;
//        this.email = HelperMethod.removeDotEmail(email);
//        this.password = password;
//    }

    public SignupRequest(
            @NotBlank(message = "Please provide username")
            @Size(min = 6, max = 30, message = "Username has at least 6 and max 30 characters")
            String username,
            @Email(message = "Please provide valid email")
            @NotBlank(message = "Please provide your email")
            @Size(max = 50, message = "Email too long")
            String email,
            @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
            message = "Password must has an uppercase, an lower case and a numeric character")
            @NotBlank(message = "Please provide password")
            @Size(max = 200, message = "Password too long")
            String password, Set<String> role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = HelperMethod.removeDotEmail(email);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}
