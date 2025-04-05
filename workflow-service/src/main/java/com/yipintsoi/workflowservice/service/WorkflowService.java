package com.yipintsoi.workflowservice.service;

import com.yipintsoi.workflowservice.domain.dto.ProcessDefinitionDTO;
import com.yipintsoi.workflowservice.domain.dto.ProcessInstanceDTO;
import java.util.List;

public interface WorkflowService {
    List<ProcessDefinitionDTO> getProcessDefinitions();
    ProcessInstanceDTO startProcessInstance(String processDefinitionKey);
    ProcessInstanceDTO getProcessInstance(String instanceId);
}
