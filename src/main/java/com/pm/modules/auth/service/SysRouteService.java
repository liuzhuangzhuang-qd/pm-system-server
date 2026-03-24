package com.pm.modules.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pm.modules.auth.entity.SysRoute;

import java.util.Map;

/** 前端路由/菜单服务：组装 constantRoutes + asyncRoutes，按角色过滤 */
public interface SysRouteService extends IService<SysRoute> {

    /** 返回前端路由结构：constantRoutes、asyncRoutes（按当前用户角色过滤） */
    Map<String, Object> getFrontRoutes();

    /**
     * 返回前端路由结构：constantRoutes、asyncRoutes（不做角色过滤）
     * 用于前端路由 CRUD 管理界面以 tree 结构展示。
     */
    Map<String, Object> getRouteTree(String routeType, Long parentId);
}

