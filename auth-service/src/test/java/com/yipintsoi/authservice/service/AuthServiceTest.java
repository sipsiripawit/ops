package com.yipintsoi.authservice.service;

import com.yipintsoi.authservice.domain.dto.LoginRequest;
import com.yipintsoi.authservice.exception.CustomException;
import com.yipintsoi.authservice.repository.UserRepository;
import com.yipintsoi.authservice.repository.UserSessionRepository;
import com.yipintsoi.authservice.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSessionRepository userSessionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    public AuthServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_UserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("password");

        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomException.class, () -> {
            authService.login(request);
        });
        assertTrue(exception.getMessage().contains("User not found"));
    }
    
    // เพิ่ม test สำหรับ refreshToken และกรณี login สำเร็จได้ตามต้องการ
}
