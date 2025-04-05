package com.yipintsoi.userservice.domain.dto;

import lombok.Data;

@Data
public class NotificationLogDTO {
    private Long id;
    private String notificationType;
    private String message;
    private Integer userId;
}
