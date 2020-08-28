package com.app.manager.service.implementClass;

import com.app.manager.context.repository.RoleRepository;
import com.app.manager.context.repository.UserRepository;
import com.app.manager.entity.ERole;
import com.app.manager.entity.Role;
import com.app.manager.entity.User;
import com.app.manager.model.payload.request.FaceDefinitionClientRequest;
import com.app.manager.model.payload.request.FaceDefinitionServerRequest;
import com.app.manager.model.payload.response.FaceDefinitionServerResponse;
import com.app.manager.model.payload.response.UserProfileResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.*;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class UserServiceImp implements UserService {
    @Autowired UserRepository userRepository;
    @Autowired RoleRepository roleRepository;

    private static final String faceCheckHost = "";

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
    public DatabaseQueryResult saveUser(User user, Set<String> strRoles, String subscribtionId) {
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
            user.setSubscriptionId(subscribtionId);
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
    public List<UserProfileResponse> findAll(Specification<User> specification,
                                             String currentUsername) {
        try {
            var currentUser = userRepository.
                    findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("Internal Server Error"));

            var roleTeacher = roleRepository.findByName(ERole.ROLE_TEACHER)
                    .orElseThrow(() -> new RuntimeException("Internal Server Error"));

            List<User> users = userRepository.findAll(specification);
            List<UserProfileResponse> list = new ArrayList<>();

            users.forEach(user -> list.add(user.getProfile_visibility() == User.VisibilityEnum.PUBLIC ||
                    user.getProfile_visibility() == User.VisibilityEnum.TEACHER
                            && currentUser.getRoles().contains(roleTeacher) ? new UserProfileResponse(
                    user.getId(), user.getUsername(), user.getEmail(),
                    user.getFullname(), user.getPhone(), user.getAddress(),
                    user.getCivil_id(), user.getBirthday(), user.getGender()) :
                    new UserProfileResponse(user.getId(), user.getUsername())));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public DatabaseQueryResult faceCheckDefinition
            (FaceDefinitionClientRequest faceDefinitionClientRequest, String currentUsername) {
        var student = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        var faceDefinition = new FaceDefinitionServerRequest(
                    faceDefinitionClientRequest.getImg_urls(),
                    student.isFacedefinition()
                            && student.getFacedefinitionid() != null
                            && !student.getFacedefinitionid().isEmpty(),
                        student.isFacedefinition()
                            && student.getFacedefinitionid() != null
                            && !student.getFacedefinitionid().isEmpty()?
                        student.getFacedefinitionid() : "");

        var result = sentNewFaceDefinition(faceDefinition);
        if(result.isEmpty() || !result.get().isSuccess())
            return new DatabaseQueryResult(false,
                    "Setup face definition fail",
                    HttpStatus.INTERNAL_SERVER_ERROR, "");

        student.setFacedefinition(true);
        student.setFacedefinitionid(faceDefinition.getDefinitionId());
        userRepository.save(student);
        return new DatabaseQueryResult(true,
                "Setup face definition success", HttpStatus.OK, "");
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

    private Optional<FaceDefinitionServerResponse>
        sentNewFaceDefinition(FaceDefinitionServerRequest faceDefinitionServerRequest){
        var entity = new HttpEntity<>(faceDefinitionServerRequest, new HttpHeaders());
        var restTemplate = new RestTemplate();

        var response = restTemplate
                .exchange(faceCheckHost, HttpMethod.POST,
                        entity, FaceDefinitionServerResponse.class);
        return response.getBody() != null ?
                Optional.of(response.getBody()) : Optional.empty();
    }
}
