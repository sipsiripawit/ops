package com.yipintsoi.repairservice.repository;

import com.yipintsoi.repairservice.domain.entity.RepairOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {
}
