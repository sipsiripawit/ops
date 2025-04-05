package com.yipintsoi.repairservice.controller;

import com.yipintsoi.repairservice.domain.dto.RepairOrderDTO;
import com.yipintsoi.repairservice.response.ApiResponse;
import com.yipintsoi.repairservice.service.RepairOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/repair-orders")
public class RepairOrderController {

    private final RepairOrderService repairOrderService;
    
    public RepairOrderController(RepairOrderService repairOrderService) {
        this.repairOrderService = repairOrderService;
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<RepairOrderDTO>>> getAllRepairOrders() {
        List<RepairOrderDTO> orders = repairOrderService.getAllRepairOrders();
        ApiResponse<List<RepairOrderDTO>> response = ApiResponse.<List<RepairOrderDTO>>builder()
                .status("success")
                .data(orders)
                .message("Fetched repair orders successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RepairOrderDTO>> getRepairOrderById(@PathVariable Long id) {
        RepairOrderDTO order = repairOrderService.getRepairOrderById(id);
        ApiResponse<RepairOrderDTO> response = ApiResponse.<RepairOrderDTO>builder()
                .status("success")
                .data(order)
                .message("Fetched repair order successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<RepairOrderDTO>> createRepairOrder(@RequestBody RepairOrderDTO repairOrderDTO) {
        RepairOrderDTO created = repairOrderService.createRepairOrder(repairOrderDTO);
        ApiResponse<RepairOrderDTO> response = ApiResponse.<RepairOrderDTO>builder()
                .status("success")
                .data(created)
                .message("Repair order created successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RepairOrderDTO>> updateRepairOrder(@PathVariable Long id, @RequestBody RepairOrderDTO repairOrderDTO) {
        RepairOrderDTO updated = repairOrderService.updateRepairOrder(id, repairOrderDTO);
        ApiResponse<RepairOrderDTO> response = ApiResponse.<RepairOrderDTO>builder()
                .status("success")
                .data(updated)
                .message("Repair order updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRepairOrder(@PathVariable Long id) {
        repairOrderService.deleteRepairOrder(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("success")
                .message("Repair order deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
