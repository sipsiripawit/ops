package com.yipintsoi.authservice.repository;

import com.yipintsoi.authservice.domain.entity.User;
import com.yipintsoi.authservice.domain.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    List<UserSession> findByUserAndActive(User user, Boolean active);
    Optional<UserSession> findByRefreshToken(String refreshToken);
}
