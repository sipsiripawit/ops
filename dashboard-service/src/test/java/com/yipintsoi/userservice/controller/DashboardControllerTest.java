package com.yipintsoi.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yipintsoi.userservice.domain.dto.DashboardTemplateDTO;
import com.yipintsoi.userservice.response.ApiResponse;
import com.yipintsoi.userservice.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private DashboardService dashboardService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testGetAllTemplates() throws Exception {
        DashboardTemplateDTO t1 = new DashboardTemplateDTO();
        t1.setId(1L);
        t1.setTemplateName("Template A");
        t1.setTemplateConfig("{\"layout\":\"grid\"}");
        
        DashboardTemplateDTO t2 = new DashboardTemplateDTO();
        t2.setId(2L);
        t2.setTemplateName("Template B");
        t2.setTemplateConfig("{\"layout\":\"list\"}");
        
        when(dashboardService.getAllTemplates()).thenReturn(Arrays.asList(t1, t2));
        
        mockMvc.perform(get("/dashboard/templates")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.message").value("Fetched templates successfully"));
    }
}
