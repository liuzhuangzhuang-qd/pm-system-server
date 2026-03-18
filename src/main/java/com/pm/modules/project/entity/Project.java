package com.pm.modules.project.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("project")
public class Project extends BaseEntity {

    private String projectName;
    private String code;
    private String description;
    private Long managerId;
    /**
     * 0草稿1进行中2归档
     */
    private String status;
}

