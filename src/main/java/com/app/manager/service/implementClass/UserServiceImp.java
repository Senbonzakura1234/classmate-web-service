package com.app.manager.service.implementClass;

import com.app.manager.entity.ERole;
import com.app.manager.entity.Role;
import com.app.manager.entity.User;
import com.app.manager.model.payload.response.UserProfileResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.context.repository.RoleRepository;
import com.app.manager.context.repository.UserRepository;
import com.app.manager.service.interfaceClass.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    UserRepository userRepository;

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    RoleRepository roleRepository;


    @Override
    public Optional<User> findUser(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> checkExistUsername(String username) {
        try {
            return Optional.of(userRepository.existsByUsername(username));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> checkExistEmail(String email) {
        try {
            return Optional.of(userRepository.existsByEmail(email));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public DatabaseQueryResult saveUser(User user, Set<String> strRoles) {
        try {
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                var userRole = getRoleInstant(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException(
                                MessageFormat.format("Error: Cannot create role {0}.",
                                        ERole.ROLE_USER.getName())));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    if (ERole.ROLE_ADMIN.getName().equals(role)) {
                        var adminRole = getRoleInstant(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(
                                        MessageFormat.format("Error: Cannot create role {0}.",
                                                ERole.ROLE_ADMIN.getName())));
                        roles.add(adminRole);
                    } else if (ERole.ROLE_TEACHER.getName().equals(role)) {
                        var teacherRole = getRoleInstant(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException(
                                        MessageFormat.format("Error: Cannot create role {0}.",
                                                ERole.ROLE_TEACHER.getName())));
                        roles.add(teacherRole);
                    } else if (ERole.ROLE_STUDENT.getName().equals(role)) {
                        var studentRole = getRoleInstant(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException(
                                        MessageFormat.format("Error: Cannot create role {0}.",
                                                ERole.ROLE_STUDENT.getName())));
                        roles.add(studentRole);
                    } else {
                        var userRole = getRoleInstant(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(
                                        MessageFormat.format("Error: Cannot create role {0}.",
                                                ERole.ROLE_USER.getName())));
                        roles.add(userRole);
                    }
                });
            }
            user.setRoles(roles);
            userRepository.save(user);
            return new DatabaseQueryResult(true,
                    "User registered successfully!", HttpStatus.OK, "");
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "User register failed, " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public Page<UserProfileResponse> findAll(Specification<User> specification,
                                             Pageable pageable, String currentUsername) {
        try {
            var currentUser = userRepository.
                    findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("Internal Server Error"));

            var roleTeacher = roleRepository.findByName(ERole.ROLE_TEACHER)
                    .orElseThrow(() -> new RuntimeException("Internal Server Error"));

            Page<User> users = userRepository.findAll(specification, pageable);
            return users.map(user -> {
                if(user.getProfile_visibility() == User.VisibilityEnum.PUBLIC){
                    return new UserProfileResponse(
                            user.getId(), user.getUsername(), user.getEmail(),
                            user.getFullname(), user.getPhone(), user.getAddress(),
                            user.getCivil_id(), user.getBirthday(), user.getGender());
                }else if(user.getProfile_visibility() == User.VisibilityEnum.TEACHER
                    && currentUser.getRoles().contains(roleTeacher)){
                    return new UserProfileResponse(
                            user.getId(), user.getUsername(), user.getEmail(),
                            user.getFullname(), user.getPhone(), user.getAddress(),
                            user.getCivil_id(), user.getBirthday(), user.getGender());
                }else {
                    return new UserProfileResponse(user.getId(), user.getUsername());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Page.empty();
        }
    }

    private Optional<Role> getRoleInstant(ERole roleName){
        try {
            var role = roleRepository.findByName(roleName);
            if(role.isPresent()) return role;
            var newRole = new Role(roleName);
            roleRepository.save(newRole);
            return Optional.of(newRole);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

}
