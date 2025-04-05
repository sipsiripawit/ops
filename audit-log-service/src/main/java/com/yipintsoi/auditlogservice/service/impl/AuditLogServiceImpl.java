package com.yipintsoi.auditlogservice.service.impl;

import com.yipintsoi.auditlogservice.domain.dto.AuditLogDTO;
import com.yipintsoi.auditlogservice.domain.entity.AuditLog;
import com.yipintsoi.auditlogservice.domain.mapper.AuditLogMapper;
import com.yipintsoi.auditlogservice.repository.AuditLogRepository;
import com.yipintsoi.auditlogservice.service.AuditLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository, AuditLogMapper auditLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
    }

    @Override
    public AuditLogDTO createAuditLog(AuditLogDTO auditLogDTO) {
        AuditLog log = auditLogMapper.dtoToAuditLog(auditLogDTO);
        log.setCreatedAt(LocalDateTime.now());
        AuditLog saved = auditLogRepository.save(log);
        return auditLogMapper.auditLogToDTO(saved);
    }

    @Override
    public List<AuditLogDTO> getAllAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(auditLogMapper::auditLogToDTO)
                .collect(Collectors.toList());
    }
}
