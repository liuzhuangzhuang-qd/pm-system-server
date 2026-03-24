package com.pm.modules.project.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("project")
public class Project extends BaseEntity {

    /** 项目名称 */
    private String projectName;
    /** 项目编码 */
    private String code;
    /** 项目描述 */
    private String description;
    /** 负责人 ID */
    private Long managerId;
    /** 状态：0 草稿 1 进行中 2 归档 */
    private String status;
}

