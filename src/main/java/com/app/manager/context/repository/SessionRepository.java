package com.app.manager.context.repository;

import com.app.manager.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String>,
        JpaSpecificationExecutor<Session> {
    Optional<Session> findFirstByName (String name);
    Optional<Session> findFirstByCourse_idAndStatus(String course_id, Session.StatusEnum status);
    List<Session> findAllByCourse_idAndStatusIsNot(String course_id, Session.StatusEnum status);
}
