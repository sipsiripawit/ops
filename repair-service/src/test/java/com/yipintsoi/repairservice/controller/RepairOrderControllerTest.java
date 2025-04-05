package com.yipintsoi.repairservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yipintsoi.repairservice.domain.dto.RepairOrderDTO;
import com.yipintsoi.repairservice.service.RepairOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepairOrderController.class)
public class RepairOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private RepairOrderService repairOrderService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testGetAllRepairOrders() throws Exception {
        RepairOrderDTO o1 = new RepairOrderDTO();
        o1.setId(1L);
        o1.setDescription("Fix brake issue");
        o1.setStatus("PENDING");
        
        RepairOrderDTO o2 = new RepairOrderDTO();
        o2.setId(2L);
        o2.setDescription("Replace tire");
        o2.setStatus("IN_PROGRESS");
        
        when(repairOrderService.getAllRepairOrders()).thenReturn(Arrays.asList(o1, o2));
        
        mockMvc.perform(get("/repair-orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.message").value("Fetched repair orders successfully"));
    }
}
