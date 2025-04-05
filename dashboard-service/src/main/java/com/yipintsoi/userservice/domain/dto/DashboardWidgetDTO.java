package com.yipintsoi.userservice.domain.dto;

import lombok.Data;

@Data
public class DashboardWidgetDTO {
    private Long id;
    private Long userDashboardId;
    private String widgetType;
    private String widgetConfig;
}
