package com.yipintsoi.vehicleservice.service.impl;

import com.yipintsoi.vehicleservice.domain.dto.VehicleDTO;
import com.yipintsoi.vehicleservice.domain.entity.Vehicle;
import com.yipintsoi.vehicleservice.domain.mapper.VehicleMapper;
import com.yipintsoi.vehicleservice.exception.CustomException;
import com.yipintsoi.vehicleservice.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .map(vehicleMapper::vehicleToVehicleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Vehicle not found with id: " + id));
        return vehicleMapper.vehicleToVehicleDTO(vehicle);
    }

    @Override
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleMapper.vehicleDTOToVehicle(vehicleDTO);
        vehicle.setCreatedAt(LocalDateTime.now());
        vehicle.setUpdatedAt(LocalDateTime.now());
        Vehicle saved = vehicleRepository.save(vehicle);
        return vehicleMapper.vehicleToVehicleDTO(saved);
    }

    @Override
    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Vehicle existing = vehicleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Vehicle not found with id: " + id));
        existing.setLicensePlate(vehicleDTO.getLicensePlate());
        existing.setModel(vehicleDTO.getModel());
        existing.setStatus(vehicleDTO.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());
        Vehicle updated = vehicleRepository.save(existing);
        return vehicleMapper.vehicleToVehicleDTO(updated);
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle existing = vehicleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Vehicle not found with id: " + id));
        vehicleRepository.delete(existing);
    }
}
