package com.yipintsoi.userservice.repository;

import com.yipintsoi.userservice.domain.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
}
