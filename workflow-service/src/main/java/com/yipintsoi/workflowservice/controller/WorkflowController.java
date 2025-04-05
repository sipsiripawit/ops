package com.yipintsoi.workflowservice.controller;

import com.yipintsoi.workflowservice.domain.dto.ProcessDefinitionDTO;
import com.yipintsoi.workflowservice.domain.dto.ProcessInstanceDTO;
import com.yipintsoi.workflowservice.response.ApiResponse;
import com.yipintsoi.workflowservice.service.WorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;
    
    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }
    
    // ดึง Process Definitions ทั้งหมด
    @GetMapping("/definitions")
    public ResponseEntity<ApiResponse<List<ProcessDefinitionDTO>>> getProcessDefinitions() {
        List<ProcessDefinitionDTO> definitions = workflowService.getProcessDefinitions();
        ApiResponse<List<ProcessDefinitionDTO>> response = ApiResponse.<List<ProcessDefinitionDTO>>builder()
                .status("success")
                .data(definitions)
                .message("Fetched process definitions successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    // เริ่มต้น Process Instance ด้วย processDefinitionKey
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<ProcessInstanceDTO>> startProcessInstance(@RequestParam String processDefinitionKey) {
        ProcessInstanceDTO instance = workflowService.startProcessInstance(processDefinitionKey);
        ApiResponse<ProcessInstanceDTO> response = ApiResponse.<ProcessInstanceDTO>builder()
                .status("success")
                .data(instance)
                .message("Process instance started successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    // ดึงข้อมูล Process Instance ตาม id
    @GetMapping("/instance/{id}")
    public ResponseEntity<ApiResponse<ProcessInstanceDTO>> getProcessInstance(@PathVariable String id) {
        ProcessInstanceDTO instance = workflowService.getProcessInstance(id);
        ApiResponse<ProcessInstanceDTO> response = ApiResponse.<ProcessInstanceDTO>builder()
                .status("success")
                .data(instance)
                .message("Fetched process instance successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
