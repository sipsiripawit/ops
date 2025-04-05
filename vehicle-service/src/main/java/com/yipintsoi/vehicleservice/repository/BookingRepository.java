package com.yipintsoi.vehicleservice.repository;

import com.yipintsoi.vehicleservice.domain.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
