package com.yipintsoi.userservice.service.impl;

import com.yipintsoi.userservice.domain.dto.UserProfileDTO;
import com.yipintsoi.userservice.domain.entity.UserProfile;
import com.yipintsoi.userservice.domain.mapper.UserProfileMapper;
import com.yipintsoi.userservice.repository.UserProfileRepository;
import com.yipintsoi.userservice.service.UserProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

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
                .orElseThrow(() -> new RuntimeException("User profile not found"));
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
        userProfileRepository.deleteById(id);
    }

    @Override
    public UserProfileDTO getUserProfileById(Long id) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User profile not found"));
        return userProfileMapper.userProfileToDTO(profile);
    }

    @Override
    public List<UserProfileDTO> getAllUserProfiles() {
        return userProfileRepository.findAll().stream()
                .map(userProfileMapper::userProfileToDTO)
                .collect(Collectors.toList());
    }
}
