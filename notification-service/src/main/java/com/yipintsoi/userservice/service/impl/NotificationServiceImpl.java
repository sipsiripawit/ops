package com.yipintsoi.userservice.service.impl;

import com.yipintsoi.userservice.domain.dto.AuditLogDTO;
import com.yipintsoi.userservice.domain.dto.NotificationLogDTO;
import com.yipintsoi.userservice.domain.entity.AuditLog;
import com.yipintsoi.userservice.domain.entity.NotificationLog;
import com.yipintsoi.userservice.domain.mapper.AuditLogMapper;
import com.yipintsoi.userservice.domain.mapper.NotificationLogMapper;
import com.yipintsoi.userservice.repository.AuditLogRepository;
import com.yipintsoi.userservice.repository.NotificationLogRepository;
import com.yipintsoi.userservice.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationLogRepository notificationLogRepository;
    private final AuditLogRepository auditLogRepository;
    private final NotificationLogMapper notificationLogMapper;
    private final AuditLogMapper auditLogMapper;

    public NotificationServiceImpl(NotificationLogRepository notificationLogRepository,
                                   AuditLogRepository auditLogRepository,
                                   NotificationLogMapper notificationLogMapper,
                                   AuditLogMapper auditLogMapper) {
        this.notificationLogRepository = notificationLogRepository;
        this.auditLogRepository = auditLogRepository;
        this.notificationLogMapper = notificationLogMapper;
        this.auditLogMapper = auditLogMapper;
    }

    @Override
    public NotificationLogDTO createNotification(NotificationLogDTO notificationLogDTO) {
        NotificationLog log = notificationLogMapper.dtoToNotificationLog(notificationLogDTO);
        log.setCreatedAt(LocalDateTime.now());
        log.setUpdatedAt(LocalDateTime.now());
        NotificationLog saved = notificationLogRepository.save(log);
        return notificationLogMapper.notificationLogToDTO(saved);
    }

    @Override
    public AuditLogDTO createAuditLog(AuditLogDTO auditLogDTO) {
        AuditLog log = auditLogMapper.dtoToAuditLog(auditLogDTO);
        log.setCreatedAt(LocalDateTime.now());
        AuditLog saved = auditLogRepository.save(log);
        return auditLogMapper.auditLogToDTO(saved);
    }

    @Override
    public List<NotificationLogDTO> getAllNotificationLogs() {
        return notificationLogRepository.findAll().stream()
                .map(notificationLogMapper::notificationLogToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogDTO> getAllAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(auditLogMapper::auditLogToDTO)
                .collect(Collectors.toList());
    }
}
