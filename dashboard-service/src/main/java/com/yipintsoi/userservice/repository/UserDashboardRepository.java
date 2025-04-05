package com.yipintsoi.userservice.repository;

import com.yipintsoi.userservice.domain.entity.UserDashboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDashboardRepository extends JpaRepository<UserDashboard, Long> {
}
