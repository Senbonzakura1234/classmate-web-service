package com.app.manager.context.repository;

import com.app.manager.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String>, JpaSpecificationExecutor<Session> {
    Optional<Session> findFirstByName (String name);
}
