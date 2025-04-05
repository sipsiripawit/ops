package com.yipintsoi.auditlogservice.service;

import com.yipintsoi.auditlogservice.domain.dto.AuditLogDTO;
import java.util.List;

public interface AuditLogService {
    AuditLogDTO createAuditLog(AuditLogDTO auditLogDTO);
    List<AuditLogDTO> getAllAuditLogs();
}
