package com.app.manager.service.interfaceClass;

import com.app.manager.entity.User;
import com.app.manager.model.payload.request.FaceDefinitionClientRequest;
import com.app.manager.model.payload.request.UserProfileRequest;
import com.app.manager.model.payload.response.UserProfileResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    Optional<User> findUser(String username);
    Optional<Boolean> checkExistUsername(String username);
    Optional<Boolean> checkExistEmail(String email);
    DatabaseQueryResult saveUser(User user, Set<String> strRoles);
    List<UserProfileResponse> findAll(Specification<User> specification, String currentUsername);
    Optional<UserProfileResponse> userProfile(String id, String currentUsername);
    DatabaseQueryResult updateProfile(UserProfileRequest userProfileRequest, String currentUsername);
    DatabaseQueryResult faceCheckDefinition
            (FaceDefinitionClientRequest faceDefinitionClientRequest, String currentUsername);

}
