package com.pm.modules.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.auth.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/** 用户-角色关联表 Mapper */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
}

