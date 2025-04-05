package com.yipintsoi.userservice.domain.mapper;

import com.yipintsoi.userservice.domain.dto.UserProfileDTO;
import com.yipintsoi.userservice.domain.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    UserProfileDTO userProfileToDTO(UserProfile profile);
    UserProfile dtoToUserProfile(UserProfileDTO dto);
}
