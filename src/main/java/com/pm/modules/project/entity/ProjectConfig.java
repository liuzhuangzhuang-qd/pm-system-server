package com.pm.modules.project.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("project_config")
public class ProjectConfig extends BaseEntity {

    /** 项目 ID */
    private Long projectId;
    /** 工作流配置 JSON */
    private String workflowConfig;
    /** 自定义字段 JSON */
    private String customFields;
}

