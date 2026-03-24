package com.pm.modules.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.auth.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

/** 系统角色表 Mapper */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
}

