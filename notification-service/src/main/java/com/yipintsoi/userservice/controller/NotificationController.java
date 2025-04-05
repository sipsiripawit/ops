package com.yipintsoi.userservice.controller;

import com.yipintsoi.userservice.domain.dto.NotificationLogDTO;
import com.yipintsoi.userservice.domain.dto.AuditLogDTO;
import com.yipintsoi.userservice.response.ApiResponse;
import com.yipintsoi.userservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @PostMapping("/log")
    public ResponseEntity<ApiResponse<NotificationLogDTO>> createNotification(@RequestBody NotificationLogDTO notificationLogDTO) {
        NotificationLogDTO created = notificationService.createNotification(notificationLogDTO);
        ApiResponse<NotificationLogDTO> response = ApiResponse.<NotificationLogDTO>builder()
                .status("success")
                .data(created)
                .message("Notification log created successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/audit")
    public ResponseEntity<ApiResponse<AuditLogDTO>> createAuditLog(@RequestBody AuditLogDTO auditLogDTO) {
        AuditLogDTO created = notificationService.createAuditLog(auditLogDTO);
        ApiResponse<AuditLogDTO> response = ApiResponse.<AuditLogDTO>builder()
                .status("success")
                .data(created)
                .message("Audit log created successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/log")
    public ResponseEntity<ApiResponse<List<NotificationLogDTO>>> getAllNotificationLogs() {
        List<NotificationLogDTO> logs = notificationService.getAllNotificationLogs();
        ApiResponse<List<NotificationLogDTO>> response = ApiResponse.<List<NotificationLogDTO>>builder()
                .status("success")
                .data(logs)
                .message("Fetched all notification logs successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/audit")
    public ResponseEntity<ApiResponse<List<AuditLogDTO>>> getAllAuditLogs() {
        List<AuditLogDTO> logs = notificationService.getAllAuditLogs();
        ApiResponse<List<AuditLogDTO>> response = ApiResponse.<List<AuditLogDTO>>builder()
                .status("success")
                .data(logs)
                .message("Fetched all audit logs successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
