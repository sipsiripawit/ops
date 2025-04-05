package com.yipintsoi.userservice.domain.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private Long id;
    private Integer authUserId;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String preferences;
}
