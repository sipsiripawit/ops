package com.yipintsoi.repairservice.domain.mapper;

import com.yipintsoi.repairservice.domain.dto.RepairOrderDTO;
import com.yipintsoi.repairservice.domain.entity.RepairOrder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RepairOrderMapper {
    RepairOrderMapper INSTANCE = Mappers.getMapper(RepairOrderMapper.class);

    RepairOrderDTO repairOrderToRepairOrderDTO(RepairOrder repairOrder);
    RepairOrder repairOrderDTOToRepairOrder(RepairOrderDTO repairOrderDTO);
}
