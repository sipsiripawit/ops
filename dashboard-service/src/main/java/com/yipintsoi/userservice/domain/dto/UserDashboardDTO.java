package com.yipintsoi.userservice.domain.dto;

import lombok.Data;

@Data
public class UserDashboardDTO {
    private Long id;
    private Integer userId;
    private DashboardTemplateDTO template;
}
