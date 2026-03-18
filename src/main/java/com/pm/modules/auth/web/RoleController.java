package com.pm.modules.auth.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.auth.entity.SysRole;
import com.pm.modules.auth.service.SysRoleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final SysRoleService sysRoleService;

    public RoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody SysRole role) {
        return Result.success(sysRoleService.save(role));
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        return Result.success(sysRoleService.updateById(role));
    }

    @GetMapping("/{id}")
    public Result<SysRole> get(@PathVariable Long id) {
        return Result.success(sysRoleService.getById(id));
    }

    @GetMapping
    public Result<PageResult<SysRole>> list(@RequestParam(defaultValue = "1") long pageNo,
                                            @RequestParam(defaultValue = "10") long pageSize) {
        Page<SysRole> page = sysRoleService.page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

