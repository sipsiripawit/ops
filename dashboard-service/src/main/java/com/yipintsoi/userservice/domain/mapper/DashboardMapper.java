package com.yipintsoi.userservice.domain.mapper;

import com.yipintsoi.userservice.domain.dto.DashboardTemplateDTO;
import com.yipintsoi.userservice.domain.dto.UserDashboardDTO;
import com.yipintsoi.userservice.domain.dto.DashboardWidgetDTO;
import com.yipintsoi.userservice.domain.entity.DashboardTemplate;
import com.yipintsoi.userservice.domain.entity.UserDashboard;
import com.yipintsoi.userservice.domain.entity.DashboardWidget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DashboardMapper {
    DashboardMapper INSTANCE = Mappers.getMapper(DashboardMapper.class);

    DashboardTemplateDTO dashboardTemplateToDTO(DashboardTemplate template);
    DashboardTemplate dashboardTemplateDTOToEntity(DashboardTemplateDTO dto);

    @Mapping(target = "template", source = "template")
    UserDashboardDTO userDashboardToDTO(UserDashboard dashboard);
    @Mapping(target = "template", source = "template")
    UserDashboard userDashboardDTOToEntity(UserDashboardDTO dto);

    @Mapping(source = "userDashboard.id", target = "userDashboardId")
    DashboardWidgetDTO dashboardWidgetToDTO(DashboardWidget widget);
    @Mapping(source = "userDashboardId", target = "userDashboard.id")
    DashboardWidget dashboardWidgetDTOToEntity(DashboardWidgetDTO dto);
}
