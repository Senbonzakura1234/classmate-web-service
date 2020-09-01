package com.app.manager.context.repository;

import com.app.manager.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, String> {
    List<File> findAllByStudentexercise_idAndStatus(String studentexercise_id, File.StatusEnum status);
}
