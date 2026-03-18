package com.pm.modules.project.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("project_config")
public class ProjectConfig extends BaseEntity {

    private Long projectId;
    /**
     * workflow_config JSON
     */
    private String workflowConfig;
    /**
     * custom_fields JSON
     */
    private String customFields;
}

