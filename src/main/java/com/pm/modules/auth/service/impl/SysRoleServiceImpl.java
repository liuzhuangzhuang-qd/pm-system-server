package com.pm.modules.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.auth.entity.SysRole;
import com.pm.modules.auth.mapper.SysRoleMapper;
import com.pm.modules.auth.service.SysRoleService;
import org.springframework.stereotype.Service;

/** 角色服务实现，基于 MyBatis-Plus 默认 CRUD */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}

