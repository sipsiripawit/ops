package com.yipintsoi.userservice.service;

import com.yipintsoi.userservice.domain.dto.UserProfileDTO;
import java.util.List;

public interface UserProfileService {
    UserProfileDTO createUserProfile(UserProfileDTO dto);
    UserProfileDTO updateUserProfile(Long id, UserProfileDTO dto);
    void deleteUserProfile(Long id);
    UserProfileDTO getUserProfileById(Long id);
    List<UserProfileDTO> getAllUserProfiles();
}
