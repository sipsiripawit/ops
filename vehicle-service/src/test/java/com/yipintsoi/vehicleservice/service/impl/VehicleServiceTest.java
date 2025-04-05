package com.yipintsoi.vehicleservice.service.impl;

import com.yipintsoi.vehicleservice.domain.dto.VehicleDTO;
import com.yipintsoi.vehicleservice.domain.entity.Vehicle;
import com.yipintsoi.vehicleservice.domain.mapper.VehicleMapper;
import com.yipintsoi.vehicleservice.exception.CustomException;
import com.yipintsoi.vehicleservice.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    public VehicleServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetVehicleById_NotFound() {
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(CustomException.class, () -> {
            vehicleService.getVehicleById(99L);
        });
        assertTrue(ex.getMessage().contains("Vehicle not found"));
    }
    
    // เพิ่ม test อื่น ๆ ได้ตามต้องการ
}
