package com.yipintsoi.auditlogservice.controller;

import com.yipintsoi.auditlogservice.domain.dto.AuditLogDTO;
import com.yipintsoi.auditlogservice.response.ApiResponse;
import com.yipintsoi.auditlogservice.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AuditLogDTO>> createAuditLog(@RequestBody AuditLogDTO auditLogDTO) {
        AuditLogDTO created = auditLogService.createAuditLog(auditLogDTO);
        ApiResponse<AuditLogDTO> response = ApiResponse.<AuditLogDTO>builder()
                .status("success")
                .data(created)
                .message("Audit log created successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuditLogDTO>>> getAllAuditLogs() {
        List<AuditLogDTO> logs = auditLogService.getAllAuditLogs();
        ApiResponse<List<AuditLogDTO>> response = ApiResponse.<List<AuditLogDTO>>builder()
                .status("success")
                .data(logs)
                .message("Fetched all audit logs successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
