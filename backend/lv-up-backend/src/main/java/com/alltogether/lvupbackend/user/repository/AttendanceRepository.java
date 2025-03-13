package com.alltogether.lvupbackend.user.repository;

import com.alltogether.lvupbackend.user.domain.Attendance;
import com.alltogether.lvupbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    Optional<Attendance> findByUserAndAttendanceDtBetween(User user, LocalDateTime start, LocalDateTime end);
}