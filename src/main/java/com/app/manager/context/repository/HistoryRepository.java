package com.app.manager.context.repository;

import com.app.manager.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, String> {
}
