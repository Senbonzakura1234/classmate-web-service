package com.app.manager.service.interfaceClass;

import com.app.manager.context.specification.UserSpecification;
import com.app.manager.entity.User;
import com.app.manager.model.UserModel;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.Set;

public interface UserService {
    Optional<User> findUser(String username);
    Optional<Boolean> checkExistUsername(String username);
    Optional<Boolean> checkExistEmail(String email);
    DatabaseQueryResult saveUser(User user, Set<String> strRoles);
    Page<UserModel> findAll(Specification<User> specification, Pageable pageable);
}
