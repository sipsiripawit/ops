package com.yipintsoi.userservice.service;

import com.yipintsoi.userservice.domain.dto.DashboardTemplateDTO;
import com.yipintsoi.userservice.domain.dto.UserDashboardDTO;
import com.yipintsoi.userservice.domain.dto.DashboardWidgetDTO;
import java.util.List;

public interface DashboardService {
    // Template operations
    DashboardTemplateDTO createTemplate(DashboardTemplateDTO dto);
    DashboardTemplateDTO updateTemplate(Long id, DashboardTemplateDTO dto);
    void deleteTemplate(Long id);
    List<DashboardTemplateDTO> getAllTemplates();

    // User Dashboard operations
    UserDashboardDTO createUserDashboard(UserDashboardDTO dto);
    UserDashboardDTO updateUserDashboard(Long id, UserDashboardDTO dto);
    void deleteUserDashboard(Long id);
    List<UserDashboardDTO> getUserDashboardsByUserId(Integer userId);

    // Dashboard Widget operations
    DashboardWidgetDTO createWidget(DashboardWidgetDTO dto);
    DashboardWidgetDTO updateWidget(Long id, DashboardWidgetDTO dto);
    void deleteWidget(Long id);
    List<DashboardWidgetDTO> getWidgetsByDashboardId(Long dashboardId);
}
