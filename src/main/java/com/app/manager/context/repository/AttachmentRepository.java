package com.app.manager.context.repository;

import com.app.manager.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {
    List<Attachment> findAllByPost_idAndStatus(String post_id, Attachment.StatusEnum status);
}
