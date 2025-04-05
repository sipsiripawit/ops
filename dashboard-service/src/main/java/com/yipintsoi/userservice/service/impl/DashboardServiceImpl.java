package com.yipintsoi.userservice.service.impl;

import com.yipintsoi.userservice.domain.dto.DashboardTemplateDTO;
import com.yipintsoi.userservice.domain.dto.DashboardWidgetDTO;
import com.yipintsoi.userservice.domain.dto.UserDashboardDTO;
import com.yipintsoi.userservice.domain.entity.DashboardTemplate;
import com.yipintsoi.userservice.domain.entity.DashboardWidget;
import com.yipintsoi.userservice.domain.entity.UserDashboard;
import com.yipintsoi.userservice.domain.mapper.DashboardMapper;
import com.yipintsoi.userservice.repository.DashboardTemplateRepository;
import com.yipintsoi.userservice.repository.DashboardWidgetRepository;
import com.yipintsoi.userservice.repository.UserDashboardRepository;
import com.yipintsoi.userservice.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private final DashboardTemplateRepository templateRepository;
    private final UserDashboardRepository dashboardRepository;
    private final DashboardWidgetRepository widgetRepository;
    private final DashboardMapper dashboardMapper;

    public DashboardServiceImpl(DashboardTemplateRepository templateRepository,
                                UserDashboardRepository dashboardRepository,
                                DashboardWidgetRepository widgetRepository,
                                DashboardMapper dashboardMapper) {
        this.templateRepository = templateRepository;
        this.dashboardRepository = dashboardRepository;
        this.widgetRepository = widgetRepository;
        this.dashboardMapper = dashboardMapper;
    }

    // Template operations
    @Override
    public DashboardTemplateDTO createTemplate(DashboardTemplateDTO dto) {
        DashboardTemplate template = dashboardMapper.dashboardTemplateDTOToEntity(dto);
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());
        DashboardTemplate saved = templateRepository.save(template);
        return dashboardMapper.dashboardTemplateToDTO(saved);
    }

    @Override
    public DashboardTemplateDTO updateTemplate(Long id, DashboardTemplateDTO dto) {
        DashboardTemplate existing = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        existing.setTemplateName(dto.getTemplateName());
        existing.setTemplateConfig(dto.getTemplateConfig());
        existing.setUpdatedAt(LocalDateTime.now());
        DashboardTemplate updated = templateRepository.save(existing);
        return dashboardMapper.dashboardTemplateToDTO(updated);
    }

    @Override
    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }

    @Override
    public List<DashboardTemplateDTO> getAllTemplates() {
        return templateRepository.findAll().stream()
                .map(dashboardMapper::dashboardTemplateToDTO)
                .collect(Collectors.toList());
    }

    // User Dashboard operations
    @Override
    public UserDashboardDTO createUserDashboard(UserDashboardDTO dto) {
        UserDashboard dashboard = dashboardMapper.userDashboardDTOToEntity(dto);
        dashboard.setCreatedAt(LocalDateTime.now());
        dashboard.setUpdatedAt(LocalDateTime.now());
        UserDashboard saved = dashboardRepository.save(dashboard);
        return dashboardMapper.userDashboardToDTO(saved);
    }

    @Override
    public UserDashboardDTO updateUserDashboard(Long id, UserDashboardDTO dto) {
        UserDashboard existing = dashboardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User dashboard not found"));
        existing.setUserId(dto.getUserId());
        if (dto.getTemplate() != null) {
            existing.setTemplate(dashboardMapper.dashboardTemplateDTOToEntity(dto.getTemplate()));
        }
        existing.setUpdatedAt(LocalDateTime.now());
        UserDashboard updated = dashboardRepository.save(existing);
        return dashboardMapper.userDashboardToDTO(updated);
    }

    @Override
    public void deleteUserDashboard(Long id) {
        dashboardRepository.deleteById(id);
    }

    @Override
    public List<UserDashboardDTO> getUserDashboardsByUserId(Integer userId) {
        return dashboardRepository.findAll().stream()
                .filter(d -> d.getUserId().equals(userId))
                .map(dashboardMapper::userDashboardToDTO)
                .collect(Collectors.toList());
    }

    // Dashboard Widget operations
    @Override
    public DashboardWidgetDTO createWidget(DashboardWidgetDTO dto) {
        DashboardWidget widget = dashboardMapper.dashboardWidgetDTOToEntity(dto);
        widget.setCreatedAt(LocalDateTime.now());
        widget.setUpdatedAt(LocalDateTime.now());
        DashboardWidget saved = widgetRepository.save(widget);
        return dashboardMapper.dashboardWidgetToDTO(saved);
    }

    @Override
    public DashboardWidgetDTO updateWidget(Long id, DashboardWidgetDTO dto) {
        DashboardWidget existing = widgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dashboard widget not found"));
        existing.setWidgetType(dto.getWidgetType());
        existing.setWidgetConfig(dto.getWidgetConfig());
        existing.setUpdatedAt(LocalDateTime.now());
        DashboardWidget updated = widgetRepository.save(existing);
        return dashboardMapper.dashboardWidgetToDTO(updated);
    }

    @Override
    public void deleteWidget(Long id) {
        widgetRepository.deleteById(id);
    }

    @Override
    public List<DashboardWidgetDTO> getWidgetsByDashboardId(Long dashboardId) {
        return widgetRepository.findAll().stream()
                .filter(w -> w.getUserDashboard().getId().equals(dashboardId))
                .map(dashboardMapper::dashboardWidgetToDTO)
                .collect(Collectors.toList());
    }
}
