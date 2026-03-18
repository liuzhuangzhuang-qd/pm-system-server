package com.pm.modules.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pm.modules.auth.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);
}

