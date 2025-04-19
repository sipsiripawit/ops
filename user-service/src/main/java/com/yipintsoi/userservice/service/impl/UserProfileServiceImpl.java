package com.yipintsoi.userservice.service.impl;

import com.yipintsoi.userservice.domain.dto.UserProfileDTO;
import com.yipintsoi.userservice.domain.entity.UserProfile;
import com.yipintsoi.userservice.domain.mapper.UserProfileMapper;
import com.yipintsoi.userservice.exception.CustomException;
import com.yipintsoi.userservice.repository.UserProfileRepository;
import com.yipintsoi.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfileDTO createUserProfile(UserProfileDTO dto) {
        UserProfile profile = userProfileMapper.dtoToUserProfile(dto);
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        UserProfile saved = userProfileRepository.save(profile);
        return userProfileMapper.userProfileToDTO(saved);
    }

    @Override
    public UserProfileDTO updateUserProfile(Long id, UserProfileDTO dto) {
        UserProfile existing = userProfileRepository.findById(id)
                .orElseThrow(() -> new CustomException("User profile not found with id: " + id));
        
        existing.setFullName(dto.getFullName());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setAddress(dto.getAddress());
        existing.setPreferences(dto.getPreferences());
        existing.setUpdatedAt(LocalDateTime.now());
        
        UserProfile updated = userProfileRepository.save(existing);
        return userProfileMapper.userProfileToDTO(updated);
    }

    @Override
    public void deleteUserProfile(Long id) {
        if (!userProfileRepository.existsById(id)) {
            throw new CustomException("User profile not found with id: " + id);
        }
        userProfileRepository.deleteById(id);
    }

    @Override
    public UserProfileDTO getUserProfileById(Long id) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new CustomException("User profile not found with id: " + id));
        return userProfileMapper.userProfileToDTO(profile);
    }

    @Override
    public List<UserProfileDTO> getAllUserProfiles() {
        return userProfileRepository.findAll().stream()
                .map(userProfileMapper::userProfileToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByAuthUserId(Integer authUserId) {
        return userProfileRepository.findByAuthUserId(authUserId).isPresent();
    }

    @Override
    public UserProfileDTO getUserProfileByAuthUserId(Integer authUserId) {
        UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new CustomException("User profile not found for auth user id: " + authUserId));
        return userProfileMapper.userProfileToDTO(profile);
    }

    @Override
    public UserProfileDTO createUserProfileFromAuthUser(Integer authUserId, String username, String email) {
        // ตรวจสอบว่ามีโปรไฟล์อยู่แล้วหรือไม่
        Optional<UserProfile> existingProfile = userProfileRepository.findByAuthUserId(authUserId);
        if (existingProfile.isPresent()) {
            log.warn("User profile for auth user id {} already exists", authUserId);
            return userProfileMapper.userProfileToDTO(existingProfile.get());
        }
        
        // สร้างโปรไฟล์ใหม่
        UserProfile profile = new UserProfile();
        profile.setAuthUserId(authUserId);
        profile.setFullName(username); // ใช้ username เป็น fullName เริ่มต้น (ผู้ใช้สามารถอัปเดตได้ภายหลัง)
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        
        // บันทึกลงฐานข้อมูล
        UserProfile saved = userProfileRepository.save(profile);
        log.info("Created new user profile for auth user id: {}", authUserId);
        
        return userProfileMapper.userProfileToDTO(saved);
    }

    @Override
    public void updateUserEmailByAuthUserId(Integer authUserId, String email) {
        userProfileRepository.findByAuthUserId(authUserId).ifPresent(profile -> {
            // ทำการอัปเดตข้อมูลที่จำเป็น
            profile.setUpdatedAt(LocalDateTime.now());
            // ไม่ได้อัปเดตอีเมลโดยตรงเนื่องจากไม่มีฟิลด์อีเมลในตารางโปรไฟล์
            // แต่อาจเพิ่มได้ถ้าต้องการเก็บอีเมลไว้ในโปรไฟล์ด้วย
            
            userProfileRepository.save(profile);
            log.info("Updated profile for auth user id: {}", authUserId);
        });
    }
}