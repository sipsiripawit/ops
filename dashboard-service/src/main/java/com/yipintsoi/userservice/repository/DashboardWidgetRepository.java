package com.yipintsoi.userservice.repository;

import com.yipintsoi.userservice.domain.entity.DashboardWidget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardWidgetRepository extends JpaRepository<DashboardWidget, Long> {
}
