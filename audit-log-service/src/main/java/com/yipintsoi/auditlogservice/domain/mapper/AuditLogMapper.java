package com.yipintsoi.auditlogservice.domain.mapper;

import com.yipintsoi.auditlogservice.domain.dto.AuditLogDTO;
import com.yipintsoi.auditlogservice.domain.entity.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
    AuditLogMapper INSTANCE = Mappers.getMapper(AuditLogMapper.class);

    AuditLogDTO auditLogToDTO(AuditLog auditLog);
    AuditLog dtoToAuditLog(AuditLogDTO dto);
}
