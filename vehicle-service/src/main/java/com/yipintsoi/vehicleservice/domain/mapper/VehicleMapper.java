package com.yipintsoi.vehicleservice.domain.mapper;

import com.yipintsoi.vehicleservice.domain.dto.VehicleDTO;
import com.yipintsoi.vehicleservice.domain.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);

    VehicleDTO vehicleToVehicleDTO(Vehicle vehicle);
    Vehicle vehicleDTOToVehicle(VehicleDTO vehicleDTO);
}
