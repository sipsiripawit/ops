package com.yipintsoi.workflowservice.domain.dto;

import lombok.Data;

@Data
public class ProcessInstanceDTO {
    private String id;
    private String processDefinitionId;
    private boolean ended;
}
