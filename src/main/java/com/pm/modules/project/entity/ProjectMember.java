package com.pm.modules.project.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("project_member")
public class ProjectMember extends BaseEntity {

    private Long projectId;
    private Long userId;
    private String roleInProject;
}

