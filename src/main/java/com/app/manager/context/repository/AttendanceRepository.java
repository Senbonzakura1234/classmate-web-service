package com.app.manager.context.repository;

import com.app.manager.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, String> {
    Optional<Attendance> findFirstByUser_idAndSession_id(String userId, String sessionId);
    List<Attendance> findAllBySession_idAndStatusIsNot(String session_id, Attendance.StatusEnum status);
}
