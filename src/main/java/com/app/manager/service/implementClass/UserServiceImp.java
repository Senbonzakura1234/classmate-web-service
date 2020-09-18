package com.app.manager.service.implementClass;

import com.app.manager.context.repository.CourseRepository;
import com.app.manager.context.repository.RoleRepository;
import com.app.manager.context.repository.StudentCourseRepository;
import com.app.manager.context.repository.UserRepository;
import com.app.manager.entity.*;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.FaceDefinitionClientRequest;
import com.app.manager.model.payload.request.FaceDefinitionServerRequest;
import com.app.manager.model.payload.request.UserProfileRequest;
import com.app.manager.model.payload.response.FaceDefinitionServerResponse;
import com.app.manager.model.payload.response.UserProfileResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class UserServiceImp implements UserService {
    @Autowired UserRepository userRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired StudentCourseRepository studentCourseRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CastObject castObject;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);
    private static final String faceCheckHost = "";

    @Override
    public Optional<User> findUser(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> checkExistUsername(String username) {
        try {
            return Optional.of(userRepository.existsByUsername(username));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> checkExistEmail(String email) {
        try {
            return Optional.of(userRepository.existsByEmail(email));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
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
                                MessageFormat.format(
                                        "Error: Cannot create role {0}.",
                                        ERole.ROLE_USER.getName())));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    if (ERole.ROLE_ADMIN.getName().equals(role)) {
                        var adminRole = getRoleInstant(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(
                                        MessageFormat.format(
                                                "Error: Cannot create role {0}.",
                                                ERole.ROLE_ADMIN.getName())));
                        roles.add(adminRole);
                    } else if (ERole.ROLE_TEACHER.getName().equals(role)) {
                        var teacherRole = getRoleInstant(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException(
                                        MessageFormat.format(
                                                "Error: Cannot create role {0}.",
                                                ERole.ROLE_TEACHER.getName())));
                        roles.add(teacherRole);
                    } else if (ERole.ROLE_STUDENT.getName().equals(role)) {
                        var studentRole = getRoleInstant(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException(
                                        MessageFormat.format(
                                                "Error: Cannot create role {0}.",
                                                ERole.ROLE_STUDENT.getName())));
                        roles.add(studentRole);
                    } else {
                        var userRole = getRoleInstant(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(
                                        MessageFormat.format(
                                                "Error: Cannot create role {0}.",
                                                ERole.ROLE_USER.getName())));
                        roles.add(userRole);
                    }
                });
            }
            user.setRoles(roles);
            user.setAvatar_file_id("1m34xGnYQfstyeAFx_KsrBPhEhoTVYiVD");
            userRepository.save(user);
            return new DatabaseQueryResult(true,
                    "User registered successfully!",
                    HttpStatus.OK, castObject.profilePublic(user));
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
            var users = userRepository.findAll(specification);

            return users.stream().map(user -> new UserProfileResponse(
                    user.getId(), user.getUsername(), user.getAvatar_file_id()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<UserProfileResponse> userProfile(
            String userId, String currentUsername) {
        try {
            var userToSee = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User Not found"));
            if(userToSee.getUsername().equals(currentUsername))
                return Optional.of(castObject.profilePublic(userToSee));

            var currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("User Not found"));
            var roles = currentUser.getRoles();
            var roleAdmin = getRoleInstant(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Role Not found"));

            if(roles.contains(roleAdmin))
                return Optional.of(castObject.profilePublic(userToSee));

            if(userToSee.getProfile_visibility() == EVisibility.PRIVATE)
                return Optional.of(castObject.profilePrivate(userToSee));

            if(userToSee.getProfile_visibility() == EVisibility.PUBLIC)
                return Optional.of(castObject.profilePublic(userToSee));

            var listCourseOfUserToSee = studentCourseRepository
                    .findAllByUser_idAndStatus(userToSee.getId(),
                            StudentCourse.StatusEnum.SHOW);


            var roleTeacher = getRoleInstant(ERole.ROLE_TEACHER)
                    .orElseThrow(() -> new RuntimeException("Role Not found"));

            if(roles.contains(roleTeacher)) {
                if(userToSee.getProfile_visibility() == EVisibility.TEACHER)
                    return Optional.of(castObject.profilePublic(userToSee));
                if (listCourseOfUserToSee.stream().map(studentCourse -> courseRepository
                        .findById(studentCourse.getCourse_id()))
                        .filter(Optional::isPresent)
                        .anyMatch(course -> course.get().getUser_id()
                            .equals(currentUser.getId()))) {
                    return Optional.of(castObject.profilePublic(userToSee));
                }
            }

            var roleStudent = getRoleInstant(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Role Not found"));

            if (!roles.contains(roleStudent))
                return Optional.of(castObject.profilePrivate(userToSee));


            var listCourseOfCurrentUser = studentCourseRepository
                    .findAllByUser_idAndStatus(currentUser.getId(),
                            StudentCourse.StatusEnum.SHOW);

            return listCourseOfCurrentUser.stream().map(toKey)
                    .flatMap(key -> listCourseOfUserToSee.stream()
                            .map(toKey).filter(key::equals)).count() > 0 ?
                    Optional.of(castObject.profilePublic(userToSee))
                    : Optional.of(castObject.profilePrivate(userToSee));
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return Optional.empty();
        }
    }

    @Override
    public DatabaseQueryResult updateProfile(
            UserProfileRequest userProfileRequest, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername);
            if(currentUser.isEmpty())
                return new DatabaseQueryResult(false, "User not found",
                        HttpStatus.NOT_FOUND, userProfileRequest);
            var u = currentUser.get();
            u.setFullname(userProfileRequest.getFullname());
            u.setPhone(userProfileRequest.getPhone());
            u.setAvatar_file_id(userProfileRequest.getAvatar_file_id());
            u.setAddress(userProfileRequest.getAddress());
            u.setCivil_id(userProfileRequest.getCivil_id());
            u.setBirthday(userProfileRequest.getBirthday());
            u.setGender(userProfileRequest.getGender());
            userRepository.save(u);
            return new DatabaseQueryResult(true, "Update profile success",
                    HttpStatus.OK, castObject.profilePublic(u));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(false, "Update profile failed",
                    HttpStatus.INTERNAL_SERVER_ERROR, userProfileRequest);
        }
    }

    @Override
    public DatabaseQueryResult faceCheckDefinition
            (FaceDefinitionClientRequest faceDefinitionClientRequest, String currentUsername) {
        try {
            var student = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            var faceDefinition = new FaceDefinitionServerRequest(
                        faceDefinitionClientRequest.getFile_ids(),
                        student.isFace_definition()
                                && student.getFace_definition_id() != null
                                && !student.getFace_definition_id().isEmpty(),
                            student.isFace_definition()
                                && student.getFace_definition_id() != null
                                && !student.getFace_definition_id().isEmpty()?
                            student.getFace_definition_id() : "");

            var result = sentNewFaceDefinition(faceDefinition);
            if(result.isEmpty() || !result.get().isSuccess())
                return new DatabaseQueryResult(false,
                        "Setup face definition fail",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        faceDefinitionClientRequest);

            student.setFace_definition(true);
            student.setFace_definition_id(faceDefinition.getDefinition_id());
            userRepository.save(student);
            return new DatabaseQueryResult(true,
                    "Setup face definition success", HttpStatus.OK,
                    faceDefinitionClientRequest);
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return new DatabaseQueryResult(false,
                    "Setup face definition fail",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    faceDefinitionClientRequest);
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
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return Optional.empty();
        }
    }

    private Optional<FaceDefinitionServerResponse>
        sentNewFaceDefinition(FaceDefinitionServerRequest faceDefinitionServerRequest){
        try {
            var entity = new HttpEntity<>(faceDefinitionServerRequest, new HttpHeaders());
            var restTemplate = new RestTemplate();

            var response = restTemplate
                    .exchange(faceCheckHost, HttpMethod.POST,
                            entity, FaceDefinitionServerResponse.class);
            return response.getBody() != null ?
                    Optional.of(response.getBody()) : Optional.empty();
        } catch (RestClientException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return Optional.empty();
        }
    }

    private final Function<StudentCourse, List<Object>> toKey = p ->
            Collections.singletonList(p.getCourse_id());
}
