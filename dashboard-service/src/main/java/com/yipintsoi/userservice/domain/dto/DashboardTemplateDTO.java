package com.yipintsoi.userservice.domain.dto;

import lombok.Data;

@Data
public class DashboardTemplateDTO {
    private Long id;
    private String templateName;
    private String templateConfig;
}
