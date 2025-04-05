package com.yipintsoi.workflowservice.service.impl;

import com.yipintsoi.workflowservice.domain.dto.ProcessDefinitionDTO;
import com.yipintsoi.workflowservice.domain.dto.ProcessInstanceDTO;
import com.yipintsoi.workflowservice.domain.mapper.WorkflowMapper;
import com.yipintsoi.workflowservice.exception.CustomException;
import com.yipintsoi.workflowservice.service.WorkflowService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final WorkflowMapper workflowMapper;
    
    public WorkflowServiceImpl(RepositoryService repositoryService,
                               RuntimeService runtimeService,
                               WorkflowMapper workflowMapper) {
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.workflowMapper = workflowMapper;
    }
    
    @Override
    public List<ProcessDefinitionDTO> getProcessDefinitions() {
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        return definitions.stream()
                .map(workflowMapper::toProcessDefinitionDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProcessInstanceDTO startProcessInstance(String processDefinitionKey) {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        if (instance == null) {
            throw new CustomException("Failed to start process instance for key: " + processDefinitionKey);
        }
        return workflowMapper.toProcessInstanceDTO(instance);
    }
    
    @Override
    public ProcessInstanceDTO getProcessInstance(String instanceId) {
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(instanceId)
                .singleResult();
        if (instance == null) {
            throw new CustomException("Process instance not found with id: " + instanceId);
        }
        return workflowMapper.toProcessInstanceDTO(instance);
    }
}
