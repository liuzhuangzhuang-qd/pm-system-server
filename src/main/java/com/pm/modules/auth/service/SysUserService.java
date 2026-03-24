package com.pm.modules.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pm.modules.auth.entity.SysUser;

import java.util.List;

/** 系统用户服务：按用户名查询、查询用户角色编码 */
public interface SysUserService extends IService<SysUser> {

    /** 按登录名查询用户（单条） */
    SysUser getByUsername(String username);

    /** 用户关联角色的 role_code（小写，如 admin、manager） */
    List<String> getDbRoleCodeListByUsername(String username);
}

