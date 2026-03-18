package com.pm.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {

    private String username;
    private String password;
    private String realName;
    private String email;
    private String phone;
    private Integer status;
}

