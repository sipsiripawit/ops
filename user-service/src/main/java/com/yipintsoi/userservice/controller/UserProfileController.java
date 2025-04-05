package com.yipintsoi.userservice.controller;

import com.yipintsoi.userservice.domain.dto.UserProfileDTO;
import com.yipintsoi.userservice.response.ApiResponse;
import com.yipintsoi.userservice.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user-profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;
    
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<UserProfileDTO>> createUserProfile(@RequestBody UserProfileDTO dto) {
        UserProfileDTO created = userProfileService.createUserProfile(dto);
        ApiResponse<UserProfileDTO> response = ApiResponse.<UserProfileDTO>builder()
                .status("success")
                .data(created)
                .message("User profile created successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserProfileDTO>> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDTO dto) {
        UserProfileDTO updated = userProfileService.updateUserProfile(id, dto);
        ApiResponse<UserProfileDTO> response = ApiResponse.<UserProfileDTO>builder()
                .status("success")
                .data(updated)
                .message("User profile updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserProfile(@PathVariable Long id) {
        userProfileService.deleteUserProfile(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("success")
                .message("User profile deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getUserProfile(@PathVariable Long id) {
        UserProfileDTO profile = userProfileService.getUserProfileById(id);
        ApiResponse<UserProfileDTO> response = ApiResponse.<UserProfileDTO>builder()
                .status("success")
                .data(profile)
                .message("User profile fetched successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfileDTO>>> getAllUserProfiles() {
        List<UserProfileDTO> profiles = userProfileService.getAllUserProfiles();
        ApiResponse<List<UserProfileDTO>> response = ApiResponse.<List<UserProfileDTO>>builder()
                .status("success")
                .data(profiles)
                .message("Fetched all user profiles successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
