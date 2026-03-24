package com.pm.modules.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.auth.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/** 系统用户表 Mapper */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}

