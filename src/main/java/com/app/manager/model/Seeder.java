package com.app.manager.model;

import com.app.manager.entity.User;
import com.app.manager.model.payload.request.SignupRequest;
import com.app.manager.service.interfaceClass.CourseCategoryService;
import com.app.manager.service.interfaceClass.RoleService;
import com.app.manager.service.interfaceClass.SubscriptionService;
import com.app.manager.service.interfaceClass.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class Seeder {
    @Autowired RoleService roleService;
    @Autowired SubscriptionService subscriptionService;
    @Autowired CourseCategoryService courseCategoryService;
    @Autowired UserService userService;
    @Autowired PasswordEncoder encoder;

    private static final List<SignupRequest> userSeeds = Arrays.asList(
        new SignupRequest("senbonzakura1997", "anhdungpham090@gmail.com",
                "8mr5QyEABmiBp12D", new HashSet<>(Collections.singletonList("Admin"))) ,
        new SignupRequest("senbonzakuraTeacher1", "anhdungpham090Teacher1@gmail.com",
                "8mr5QyEABmiBp12D", new HashSet<>(Collections.singletonList("Teacher"))) ,
        new SignupRequest("senbonzakuraTeacher2", "anhdungpham090Teacher2@gmail.com",
                "8mr5QyEABmiBp12D", new HashSet<>(Collections.singletonList("Teacher"))) ,
        new SignupRequest("senbonzakuraStudent1", "anhdungpham090Student1@gmail.com",
                "8mr5QyEABmiBp12D", new HashSet<>(Collections.singletonList("Student"))) ,
        new SignupRequest("senbonzakuraStudent2", "anhdungpham090Student2@gmail.com",
                "8mr5QyEABmiBp12D", new HashSet<>(Collections.singletonList("Student"))) ,
        new SignupRequest("senbonzakuraStudent3", "anhdungpham090Student3@gmail.com",
                "8mr5QyEABmiBp12D", new HashSet<>(Collections.singletonList("Student")))
    );

    public void Seed(){
        roleService.generateRoles();
        subscriptionService.generateSubscription();
        courseCategoryService.generateCategory();

        userSeeds.forEach(userSeed -> {
            var checkUsername = userService.checkExistUsername(userSeed.getUsername());
            if (checkUsername.isEmpty() || checkUsername.get()) return;
            var checkEmail = userService.checkExistEmail(userSeed.getEmail());
            if (checkEmail.isEmpty() || checkEmail.get()) return;
            var user = new User(userSeed.getUsername(),
                    userSeed.getEmail(),
                    encoder.encode(userSeed.getPassword()));
            System.out.println(userService.saveUser(user, userSeed.getRole()).getDescription());
        });
    }
}
