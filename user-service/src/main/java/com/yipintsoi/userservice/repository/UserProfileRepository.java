package com.yipintsoi.userservice.repository;

import com.yipintsoi.userservice.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByAuthUserId(Integer authUserId);
}
