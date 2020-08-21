package com.app.manager.service.interfaceClass;

import com.app.manager.entity.User;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.Optional;
import java.util.Set;

public interface UserService {
    Optional<User> findUser(String username);
    Optional<Boolean> checkExistUsername(String username);
    Optional<Boolean> checkExistEmail(String email);
    DatabaseQueryResult saveUser(User user, Set<String> strRoles);
}
