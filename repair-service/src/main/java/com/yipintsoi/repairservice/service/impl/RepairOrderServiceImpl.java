package com.yipintsoi.repairservice.service.impl;

import com.yipintsoi.repairservice.domain.dto.RepairOrderDTO;
import com.yipintsoi.repairservice.domain.entity.RepairOrder;
import com.yipintsoi.repairservice.domain.mapper.RepairOrderMapper;
import com.yipintsoi.repairservice.exception.CustomException;
import com.yipintsoi.repairservice.repository.RepairOrderRepository;
import com.yipintsoi.repairservice.service.RepairOrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RepairOrderServiceImpl implements RepairOrderService {

    private final RepairOrderRepository repairOrderRepository;
    private final RepairOrderMapper repairOrderMapper;
    
    public RepairOrderServiceImpl(RepairOrderRepository repairOrderRepository,
                                  RepairOrderMapper repairOrderMapper) {
        this.repairOrderRepository = repairOrderRepository;
        this.repairOrderMapper = repairOrderMapper;
    }
    
    @Override
    public List<RepairOrderDTO> getAllRepairOrders() {
        List<RepairOrder> orders = repairOrderRepository.findAll();
        return orders.stream()
                .map(repairOrderMapper::repairOrderToRepairOrderDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public RepairOrderDTO getRepairOrderById(Long id) {
        RepairOrder order = repairOrderRepository.findById(id)
                        .orElseThrow(() -> new CustomException("Repair order not found with id: " + id));
        return repairOrderMapper.repairOrderToRepairOrderDTO(order);
    }
    
    // ใช้ circuit breaker ในการสร้าง repair order เพื่อจำลอง external dependency check
    @Override
    @CircuitBreaker(name = "repairServiceCircuitBreaker", fallbackMethod = "createRepairOrderFallback")
    public RepairOrderDTO createRepairOrder(RepairOrderDTO repairOrderDTO) {
        externalRepairCheck();
        
        RepairOrder order = repairOrderMapper.repairOrderDTOToRepairOrder(repairOrderDTO);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        RepairOrder saved = repairOrderRepository.save(order);
        return repairOrderMapper.repairOrderToRepairOrderDTO(saved);
    }
    
    @Override
    public RepairOrderDTO updateRepairOrder(Long id, RepairOrderDTO repairOrderDTO) {
        RepairOrder existing = repairOrderRepository.findById(id)
                        .orElseThrow(() -> new CustomException("Repair order not found with id: " + id));
        existing.setDescription(repairOrderDTO.getDescription());
        existing.setStatus(repairOrderDTO.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());
        RepairOrder updated = repairOrderRepository.save(existing);
        return repairOrderMapper.repairOrderToRepairOrderDTO(updated);
    }
    
    @Override
    public void deleteRepairOrder(Long id) {
        RepairOrder existing = repairOrderRepository.findById(id)
                        .orElseThrow(() -> new CustomException("Repair order not found with id: " + id));
        repairOrderRepository.delete(existing);
    }
    
    // จำลอง external call ที่อาจล้มเหลว (30% chance)
    private void externalRepairCheck() {
        if (Math.random() < 0.3) {
            throw new RuntimeException("Simulated external repair check failure");
        }
    }
    
    // Fallback method สำหรับ createRepairOrder เมื่อ externalRepairCheck ล้มเหลว
    private RepairOrderDTO createRepairOrderFallback(RepairOrderDTO repairOrderDTO, Throwable t) {
        throw new CustomException("External repair check failed: " + t.getMessage());
    }
}
