package com.pm.modules.project.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("project_member")
public class ProjectMember extends BaseEntity {

    /** 项目 ID */
    private Long projectId;
    /** 用户 ID */
    private Long userId;
    /** 项目内角色 */
    private String roleInProject;
}

