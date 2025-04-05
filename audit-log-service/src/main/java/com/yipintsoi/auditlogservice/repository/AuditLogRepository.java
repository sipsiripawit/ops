package com.yipintsoi.auditlogservice.repository;

import com.yipintsoi.auditlogservice.domain.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
