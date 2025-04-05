package com.yipintsoi.userservice.service;

import com.yipintsoi.userservice.domain.dto.NotificationLogDTO;
import com.yipintsoi.userservice.domain.dto.AuditLogDTO;
import java.util.List;

public interface NotificationService {
    NotificationLogDTO createNotification(NotificationLogDTO notificationLogDTO);
    AuditLogDTO createAuditLog(AuditLogDTO auditLogDTO);
    List<NotificationLogDTO> getAllNotificationLogs();
    List<AuditLogDTO> getAllAuditLogs();
}
