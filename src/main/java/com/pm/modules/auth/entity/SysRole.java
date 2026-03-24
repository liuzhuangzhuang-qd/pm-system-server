package com.pm.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_role")
public class SysRole extends BaseEntity {

    /** 角色名称 */
    private String roleName;
    /** 角色编码（如 admin/manager/dev/tester） */
    private String roleCode;
    /** 权限 JSON 字符串 */
    private String permissions;
}

