package com.pm.modules.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pm.modules.auth.entity.SysUser;

import java.util.List;

public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

    /** 用户关联角色的 role_code（小写，如 admin、manager） */
    List<String> listDbRoleCodesByUsername(String username);
}

