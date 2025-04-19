package com.yipintsoi.userservice.service;

import com.yipintsoi.userservice.domain.dto.UserProfileDTO;
import java.util.List;

public interface UserProfileService {
    UserProfileDTO createUserProfile(UserProfileDTO dto);
    UserProfileDTO updateUserProfile(Long id, UserProfileDTO dto);
    void deleteUserProfile(Long id);
    UserProfileDTO getUserProfileById(Long id);
    List<UserProfileDTO> getAllUserProfiles();
    
    // เพิ่มเมธอดใหม่สำหรับการทำงานกับ Auth Service
    boolean existsByAuthUserId(Integer authUserId);
    UserProfileDTO getUserProfileByAuthUserId(Integer authUserId);
    UserProfileDTO createUserProfileFromAuthUser(Integer authUserId, String username, String email);
    void updateUserEmailByAuthUserId(Integer authUserId, String email);
}