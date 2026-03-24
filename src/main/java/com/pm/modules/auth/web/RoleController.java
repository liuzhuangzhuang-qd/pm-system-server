package com.pm.modules.auth.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.auth.entity.SysRole;
import com.pm.modules.auth.service.SysRoleService;
import org.springframework.web.bind.annotation.*;

/** 角色管理接口：CRUD 与分页列表 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final SysRoleService sysRoleService;

    public RoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    /** 新增角色 */
    @PostMapping("/createRole")
    public Result<Boolean> createRole(@RequestBody SysRole role) {
        return Result.success(sysRoleService.save(role));
    }

    /** 按 ID 更新角色 */
    @PutMapping("/updateRole/{id}")
    public Result<Boolean> updateRole(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        return Result.success(sysRoleService.updateById(role));
    }

    /** 按 ID 获取角色 */
    @GetMapping("/getRoleById/{id}")
    public Result<SysRole> getRoleById(@PathVariable Long id) {
        return Result.success(sysRoleService.getById(id));
    }

    /** 分页查询角色列表 */
    @GetMapping("/getRoleList")
    public Result<PageResult<SysRole>> getRoleList(@RequestParam(defaultValue = "1") long pageNo,
                                            @RequestParam(defaultValue = "10") long pageSize) {
        Page<SysRole> page = sysRoleService.page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

