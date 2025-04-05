package com.yipintsoi.repairservice.service;

import com.yipintsoi.repairservice.domain.dto.RepairOrderDTO;
import java.util.List;

public interface RepairOrderService {
    List<RepairOrderDTO> getAllRepairOrders();
    RepairOrderDTO getRepairOrderById(Long id);
    RepairOrderDTO createRepairOrder(RepairOrderDTO repairOrderDTO);
    RepairOrderDTO updateRepairOrder(Long id, RepairOrderDTO repairOrderDTO);
    void deleteRepairOrder(Long id);
}
