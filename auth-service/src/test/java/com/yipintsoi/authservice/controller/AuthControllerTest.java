package com.yipintsoi.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yipintsoi.authservice.domain.dto.LoginRequest;
import com.yipintsoi.authservice.domain.dto.LoginResponse;
import com.yipintsoi.authservice.response.ApiResponse;
import com.yipintsoi.authservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        LoginResponse response = LoginResponse.builder()
                .accessToken("dummyAccessToken")
                .refreshToken("dummyRefreshToken")
                .tokenType("Bearer")
                .build();

        when(authService.login(request)).thenReturn(response);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.accessToken").value("dummyAccessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("dummyRefreshToken"))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }
}
