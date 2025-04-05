package com.yipintsoi.auditlogservice.domain.dto;

import lombok.Data;

@Data
public class AuditLogDTO {
    private Long id;
    private String action;
    private String entity;
    private Integer entityId;
    private String oldValue;
    private String newValue;
    private Integer userId;
    private String ipAddress;
    private String description;
}
