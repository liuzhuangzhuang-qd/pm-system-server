package com.pm.modules.auth.web;

import com.pm.common.result.Result;
import com.pm.modules.auth.service.SysRouteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class RouteController {

    private final SysRouteService sysRouteService;

    public RouteController(SysRouteService sysRouteService) {
        this.sysRouteService = sysRouteService;
    }

    @GetMapping("/routes")
    public Result<Map<String, Object>> routes() {
        return Result.success(sysRouteService.getFrontRoutes());
    }
}

