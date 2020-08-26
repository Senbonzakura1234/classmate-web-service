package com.app.manager.context.repository;

import com.app.manager.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, String> {
    Optional<Attendance> findFirstByUserIdAndSessionId(String userId, String sessionId);
}
