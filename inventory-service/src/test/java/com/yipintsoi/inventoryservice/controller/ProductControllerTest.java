package com.yipintsoi.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yipintsoi.inventoryservice.domain.dto.ProductDTO;
import com.yipintsoi.inventoryservice.response.ApiResponse;
import com.yipintsoi.inventoryservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Arrays;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ProductService productService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testGetAllProducts() throws Exception {
        ProductDTO p1 = new ProductDTO();
        p1.setId(1L);
        p1.setName("Product A");
        p1.setDescription("Description A");
        p1.setPrice(BigDecimal.valueOf(10.50));
        p1.setQuantity(100);
        
        ProductDTO p2 = new ProductDTO();
        p2.setId(2L);
        p2.setName("Product B");
        p2.setDescription("Description B");
        p2.setPrice(BigDecimal.valueOf(20.00));
        p2.setQuantity(50);
        
        when(productService.getAllProducts()).thenReturn(Arrays.asList(p1, p2));
        
        mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.message").value("Fetched products successfully"));
    }
}
