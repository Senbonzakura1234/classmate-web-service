package com.app.manager.context.repository;

import com.app.manager.entity.ERole;
import com.app.manager.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
