package com.yipintsoi.workflowservice.domain.mapper;

import com.yipintsoi.workflowservice.domain.dto.ProcessDefinitionDTO;
import com.yipintsoi.workflowservice.domain.dto.ProcessInstanceDTO;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkflowMapper {

    default ProcessDefinitionDTO toProcessDefinitionDTO(ProcessDefinition pd) {
        if (pd == null) {
            return null;
        }
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setId(pd.getId());
        dto.setKey(pd.getKey());
        dto.setName(pd.getName());
        dto.setVersion(pd.getVersion());
        return dto;
    }
    
    default ProcessInstanceDTO toProcessInstanceDTO(ProcessInstance pi) {
        if (pi == null) {
            return null;
        }
        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto.setId(pi.getId());
        dto.setProcessDefinitionId(pi.getProcessDefinitionId());
        dto.setEnded(pi.isEnded());
        return dto;
    }
}
