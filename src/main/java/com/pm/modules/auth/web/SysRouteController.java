package com.pm.modules.auth.web;

import com.pm.common.result.Result;
import com.pm.modules.auth.entity.SysRoute;
import com.pm.modules.auth.service.SysRouteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class SysRouteController {

    private final SysRouteService sysRouteService;

    public SysRouteController(SysRouteService sysRouteService) {
        this.sysRouteService = sysRouteService;
    }

    /** 按 ID 获取前端路由记录 */
    @GetMapping("/getRouteById/{id}")
    public Result<SysRoute> getRouteById(@PathVariable Long id) {
        return Result.success(sysRouteService.getById(id));
    }

    /** 获取前端路由结构（constantRoutes + asyncRoutes），不做角色过滤 */
    @GetMapping("/getRouteList")
    public Result<java.util.Map<String, Object>> getRouteList(@RequestParam(required = false) String routeType,
                                                                @RequestParam(required = false) Long parentId,
                                                                @RequestParam(defaultValue = "1") long pageNo,
                                                                @RequestParam(defaultValue = "10") long pageSize) {
        // pageNo/pageSize 保留给前端兼容，但 tree 结构展示时目前不分页（避免截断父子关系）
        return Result.success(sysRouteService.getRouteTree(routeType, parentId));
    }

    /** 新增前端路由记录 */
    @PostMapping("/createRoute")
    public Result<Boolean> createRoute(@RequestBody SysRoute route) {
        return Result.success(sysRouteService.save(route));
    }

    /** 按 ID 更新前端路由记录 */
    @PutMapping("/updateRoute/{id}")
    public Result<Boolean> updateRoute(@PathVariable Long id, @RequestBody SysRoute route) {
        route.setId(id);
        return Result.success(sysRouteService.updateById(route));
    }

    /** 按 ID 删除前端路由记录（逻辑删除） */
    @DeleteMapping("/deleteRoute/{id}")
    public Result<Boolean> deleteRoute(@PathVariable Long id) {
        return Result.success(sysRouteService.removeById(id));
    }
}

