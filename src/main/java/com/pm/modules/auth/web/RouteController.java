package com.pm.modules.auth.web;

import com.pm.common.result.Result;
import com.pm.modules.auth.service.SysRouteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** 前端路由/菜单下发接口（按当前用户角色过滤 asyncRoutes） */
@RestController
@RequestMapping("/api/auth")
public class RouteController {

    private final SysRouteService sysRouteService;

    public RouteController(SysRouteService sysRouteService) {
        this.sysRouteService = sysRouteService;
    }

    /** 获取前端路由树：constantRoutes + asyncRoutes */
    @GetMapping("/getRoutes")
    public Result<Map<String, Object>> getRoutes() {
        return Result.success(sysRouteService.getFrontRoutes());
    }
}

