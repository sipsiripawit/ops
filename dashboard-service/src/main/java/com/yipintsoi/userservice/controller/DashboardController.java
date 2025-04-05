package com.yipintsoi.userservice.controller;

import com.yipintsoi.userservice.domain.dto.DashboardTemplateDTO;
import com.yipintsoi.userservice.domain.dto.UserDashboardDTO;
import com.yipintsoi.userservice.domain.dto.DashboardWidgetDTO;
import com.yipintsoi.userservice.response.ApiResponse;
import com.yipintsoi.userservice.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // Dashboard Template Endpoints
    @PostMapping("/templates")
    public ResponseEntity<ApiResponse<DashboardTemplateDTO>> createTemplate(@RequestBody DashboardTemplateDTO dto) {
        DashboardTemplateDTO created = dashboardService.createTemplate(dto);
        ApiResponse<DashboardTemplateDTO> response = ApiResponse.<DashboardTemplateDTO>builder()
                .status("success")
                .data(created)
                .message("Template created successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/templates/{id}")
    public ResponseEntity<ApiResponse<DashboardTemplateDTO>> updateTemplate(@PathVariable Long id, @RequestBody DashboardTemplateDTO dto) {
        DashboardTemplateDTO updated = dashboardService.updateTemplate(id, dto);
        ApiResponse<DashboardTemplateDTO> response = ApiResponse.<DashboardTemplateDTO>builder()
                .status("success")
                .data(updated)
                .message("Template updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/templates/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(@PathVariable Long id) {
        dashboardService.deleteTemplate(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("success")
                .message("Template deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/templates")
    public ResponseEntity<ApiResponse<List<DashboardTemplateDTO>>> getAllTemplates() {
        List<DashboardTemplateDTO> templates = dashboardService.getAllTemplates();
        ApiResponse<List<DashboardTemplateDTO>> response = ApiResponse.<List<DashboardTemplateDTO>>builder()
                .status("success")
                .data(templates)
                .message("Fetched templates successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // User Dashboard Endpoints
    @PostMapping("/user-dashboards")
    public ResponseEntity<ApiResponse<UserDashboardDTO>> createUserDashboard(@RequestBody UserDashboardDTO dto) {
        UserDashboardDTO created = dashboardService.createUserDashboard(dto);
        ApiResponse<UserDashboardDTO> response = ApiResponse.<UserDashboardDTO>builder()
                .status("success")
                .data(created)
                .message("User dashboard created successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user-dashboards/{id}")
    public ResponseEntity<ApiResponse<UserDashboardDTO>> updateUserDashboard(@PathVariable Long id, @RequestBody UserDashboardDTO dto) {
        UserDashboardDTO updated = dashboardService.updateUserDashboard(id, dto);
        ApiResponse<UserDashboardDTO> response = ApiResponse.<UserDashboardDTO>builder()
                .status("success")
                .data(updated)
                .message("User dashboard updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user-dashboards/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserDashboard(@PathVariable Long id) {
        dashboardService.deleteUserDashboard(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("success")
                .message("User dashboard deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-dashboards/{userId}")
    public ResponseEntity<ApiResponse<List<UserDashboardDTO>>> getUserDashboards(@PathVariable Integer userId) {
        List<UserDashboardDTO> dashboards = dashboardService.getUserDashboardsByUserId(userId);
        ApiResponse<List<UserDashboardDTO>> response = ApiResponse.<List<UserDashboardDTO>>builder()
                .status("success")
                .data(dashboards)
                .message("Fetched user dashboards successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // Dashboard Widget Endpoints
    @PostMapping("/widgets")
    public ResponseEntity<ApiResponse<DashboardWidgetDTO>> createWidget(@RequestBody DashboardWidgetDTO dto) {
        DashboardWidgetDTO created = dashboardService.createWidget(dto);
        ApiResponse<DashboardWidgetDTO> response = ApiResponse.<DashboardWidgetDTO>builder()
                .status("success")
                .data(created)
                .message("Widget created successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/widgets/{id}")
    public ResponseEntity<ApiResponse<DashboardWidgetDTO>> updateWidget(@PathVariable Long id, @RequestBody DashboardWidgetDTO dto) {
        DashboardWidgetDTO updated = dashboardService.updateWidget(id, dto);
        ApiResponse<DashboardWidgetDTO> response = ApiResponse.<DashboardWidgetDTO>builder()
                .status("success")
                .data(updated)
                .message("Widget updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/widgets/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWidget(@PathVariable Long id) {
        dashboardService.deleteWidget(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("success")
                .message("Widget deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/widgets/{dashboardId}")
    public ResponseEntity<ApiResponse<List<DashboardWidgetDTO>>> getWidgets(@PathVariable Long dashboardId) {
        List<DashboardWidgetDTO> widgets = dashboardService.getWidgetsByDashboardId(dashboardId);
        ApiResponse<List<DashboardWidgetDTO>> response = ApiResponse.<List<DashboardWidgetDTO>>builder()
                .status("success")
                .data(widgets)
                .message("Fetched widgets successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
