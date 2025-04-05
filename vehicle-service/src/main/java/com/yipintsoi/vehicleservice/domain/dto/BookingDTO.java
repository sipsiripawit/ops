package com.yipintsoi.vehicleservice.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long id;
    private Long vehicleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
