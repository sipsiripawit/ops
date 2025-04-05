package com.yipintsoi.vehicleservice.domain.dto;

import lombok.Data;

@Data
public class VehicleDTO {
    private Long id;
    private String licensePlate;
    private String model;
    private String status;
}
