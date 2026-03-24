package com.pm.modules.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.auth.entity.SysRoute;
import org.apache.ibatis.annotations.Mapper;

/** 前端路由/菜单表 Mapper */
@Mapper
public interface SysRouteMapper extends BaseMapper<SysRoute> {
}

