package com.pm.modules.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pm.modules.auth.entity.SysRoute;

import java.util.Map;

public interface SysRouteService extends IService<SysRoute> {

    /**
     * 返回前端路由结构：
     * {
     *   constantRoutes: Route[],
     *   asyncRoutes: Route[]
     * }
     */
    Map<String, Object> getFrontRoutes();
}

