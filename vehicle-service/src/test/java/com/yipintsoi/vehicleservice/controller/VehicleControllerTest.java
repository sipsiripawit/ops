package com.yipintsoi.vehicleservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yipintsoi.vehicleservice.domain.dto.VehicleDTO;
import com.yipintsoi.vehicleservice.response.ApiResponse;
import com.yipintsoi.vehicleservice.service.VehicleService;
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

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllVehicles() throws Exception {
        VehicleDTO v1 = new VehicleDTO();
        v1.setId(1L);
        v1.setLicensePlate("ABC-123");
        v1.setModel("Toyota Camry");
        v1.setStatus("AVAILABLE");

        VehicleDTO v2 = new VehicleDTO();
        v2.setId(2L);
        v2.setLicensePlate("XYZ-789");
        v2.setModel("Honda Civic");
        v2.setStatus("IN_USE");

        when(vehicleService.getAllVehicles()).thenReturn(Arrays.asList(v1, v2));

        mockMvc.perform(get("/vehicles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.message").value("Fetched all vehicles successfully"));
    }
}
