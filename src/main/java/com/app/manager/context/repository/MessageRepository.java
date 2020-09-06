package com.app.manager.context.repository;

import com.app.manager.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findAllByCourse_idAndStatusAndPinEquals
            (String course_id, Message.StatusEnum status, boolean pinned);
    Optional<Message> findFirstByCourse_idAndStatusAndPinEquals
            (String course_id, Message.StatusEnum status, boolean pinned);
}
