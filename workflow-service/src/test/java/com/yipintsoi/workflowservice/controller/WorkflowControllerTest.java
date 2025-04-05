package com.yipintsoi.workflowservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yipintsoi.workflowservice.domain.dto.ProcessDefinitionDTO;
import com.yipintsoi.workflowservice.response.ApiResponse;
import com.yipintsoi.workflowservice.service.WorkflowService;
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

@WebMvcTest(WorkflowController.class)
public class WorkflowControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private WorkflowService workflowService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testGetProcessDefinitions() throws Exception {
        ProcessDefinitionDTO def1 = new ProcessDefinitionDTO();
        def1.setId("def1");
        def1.setKey("processA");
        def1.setName("Process A");
        def1.setVersion(1);
        
        ProcessDefinitionDTO def2 = new ProcessDefinitionDTO();
        def2.setId("def2");
        def2.setKey("processB");
        def2.setName("Process B");
        def2.setVersion(1);
        
        when(workflowService.getProcessDefinitions()).thenReturn(Arrays.asList(def1, def2));
        
        mockMvc.perform(get("/workflow/definitions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.message").value("Fetched process definitions successfully"));
    }
}
