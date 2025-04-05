package com.yipintsoi.vehicleservice.repository;

import com.yipintsoi.vehicleservice.domain.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
