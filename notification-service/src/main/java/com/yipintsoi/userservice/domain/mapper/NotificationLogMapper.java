package com.yipintsoi.userservice.domain.mapper;

import com.yipintsoi.userservice.domain.dto.NotificationLogDTO;
import com.yipintsoi.userservice.domain.entity.NotificationLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotificationLogMapper {
    NotificationLogMapper INSTANCE = Mappers.getMapper(NotificationLogMapper.class);

    NotificationLogDTO notificationLogToDTO(NotificationLog log);
    NotificationLog dtoToNotificationLog(NotificationLogDTO dto);
}
