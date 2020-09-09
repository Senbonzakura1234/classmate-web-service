package com.app.manager.context.repository;

import com.app.manager.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findAllByCourse_idAndStatusAndPinEquals
            (String course_id, Post.StatusEnum status, boolean pinned);
    Optional<Post> findFirstByCourse_idAndStatusAndPinEquals
            (String course_id, Post.StatusEnum status, boolean pinned);
}
