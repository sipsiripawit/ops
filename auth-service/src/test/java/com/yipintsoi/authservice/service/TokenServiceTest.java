package com.yipintsoi.authservice.service;

import com.yipintsoi.authservice.domain.dto.TokenResponse;
import com.yipintsoi.authservice.event.publisher.AuthEventPublisher;
import com.yipintsoi.authservice.exception.InvalidTokenException;
import com.yipintsoi.authservice.security.TokenStore;
import com.yipintsoi.authservice.security.oauth.JwtToUserConverter;
import com.yipintsoi.authservice.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;
    
    @Mock
    private TokenStore tokenStore;
    
    @Mock
    private UserDetailsService userDetailsService;
    
    @Mock
    private AuthEventPublisher eventPublisher;
    
    @Mock
    private JwtToUserConverter jwtToUserConverter;
    
    @InjectMocks
    private TokenServiceImpl tokenService;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenService, "accessTokenExpirationMs", 900000L);
        ReflectionTestUtils.setField(tokenService, "refreshTokenExpirationMs", 86400000L);
    }
    
    @Test
    void testCreateToken_WithRememberMe() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");
        
        UserDetails userDetails = new User("testuser", "password", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        
        org.springframework.security.oauth2.jwt.Jwt jwt = mock(org.springframework.security.oauth2.jwt.Jwt.class);
        when(jwt.getTokenValue()).thenReturn("accessToken");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);
        
        // When
        TokenResponse response = tokenService.createToken(authentication, true);
        
        // Then
        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        
        verify(tokenStore).saveToken(anyString(), eq("testuser"), any(Instant.class));
        verify(eventPublisher).publishLoginEvent(eq("testuser"), anyString(), anyString());
    }
    
    @Test
    void testRefreshToken_ValidToken() {
        // Given
        String refreshToken = "validRefreshToken";
        when(tokenStore.getUsernameFromToken(refreshToken)).thenReturn("testuser");
        
        UserDetails userDetails = new User("testuser", "password", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        
        org.springframework.security.oauth2.jwt.Jwt jwt = mock(org.springframework.security.oauth2.jwt.Jwt.class);
        when(jwt.getTokenValue()).thenReturn("newAccessToken");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);
        
        // When
        TokenResponse response = tokenService.refreshToken(refreshToken);
        
        // Then
        assertNotNull(response);
        assertEquals("newAccessToken", response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        
        verify(tokenStore).saveToken(anyString(), eq("testuser"), any(Instant.class));
        verify(tokenStore).removeToken(refreshToken);
    }
    
    @Test
    void testRefreshToken_InvalidToken() {
        // Given
        String refreshToken = "invalidRefreshToken";
        when(tokenStore.getUsernameFromToken(refreshToken)).thenReturn(null);
        
        // When & Then
        assertThrows(InvalidTokenException.class, () -> {
            tokenService.refreshToken(refreshToken);
        });
    }
}