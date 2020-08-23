package com.app.manager.repository;

import com.app.manager.entity.Course;
import com.app.manager.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, String> {
    Page<Session> findByNameContains(String queryName, Pageable pageable);
    Page<Session> findByNameContainsAndStatus(String queryName, Session.StatusEnum status, Pageable pageable);
    Page<Session> findByStatus(Session.StatusEnum status, Pageable pageable);
    Page<Session> findBy(Pageable pageable);
}