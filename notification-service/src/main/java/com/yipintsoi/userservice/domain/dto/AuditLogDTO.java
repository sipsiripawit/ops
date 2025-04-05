package com.yipintsoi.userservice.domain.dto;

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
}
