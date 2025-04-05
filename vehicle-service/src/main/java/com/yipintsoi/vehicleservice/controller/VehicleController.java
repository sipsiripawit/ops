package com.yipintsoi.vehicleservice.controller;

import com.yipintsoi.vehicleservice.domain.dto.VehicleDTO;
import com.yipintsoi.vehicleservice.response.ApiResponse;
import com.yipintsoi.vehicleservice.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleDTO>>> getAllVehicles() {
        List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
        ApiResponse<List<VehicleDTO>> response = ApiResponse.<List<VehicleDTO>>builder()
                .status("success")
                .data(vehicles)
                .message("Fetched all vehicles successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleDTO>> getVehicleById(@PathVariable Long id) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        ApiResponse<VehicleDTO> response = ApiResponse.<VehicleDTO>builder()
                .status("success")
                .data(vehicle)
                .message("Fetched vehicle successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VehicleDTO>> createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO created = vehicleService.createVehicle(vehicleDTO);
        ApiResponse<VehicleDTO> response = ApiResponse.<VehicleDTO>builder()
                .status("success")
                .data(created)
                .message("Vehicle created successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleDTO>> updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO updated = vehicleService.updateVehicle(id, vehicleDTO);
        ApiResponse<VehicleDTO> response = ApiResponse.<VehicleDTO>builder()
                .status("success")
                .data(updated)
                .message("Vehicle updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("success")
                .message("Vehicle deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
