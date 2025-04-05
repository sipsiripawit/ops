package com.yipintsoi.repairservice.domain.dto;

import lombok.Data;

@Data
public class RepairOrderDTO {
    private Long id;
    private String description;
    private String status; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED
}
