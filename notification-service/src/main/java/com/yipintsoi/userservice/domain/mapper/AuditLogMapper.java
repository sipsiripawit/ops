package com.yipintsoi.userservice.domain.mapper;

import com.yipintsoi.userservice.domain.dto.AuditLogDTO;
import com.yipintsoi.userservice.domain.entity.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
    AuditLogMapper INSTANCE = Mappers.getMapper(AuditLogMapper.class);

    AuditLogDTO auditLogToDTO(AuditLog log);
    AuditLog dtoToAuditLog(AuditLogDTO dto);
}
