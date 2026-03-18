package com.pm.modules.auth.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.auth.entity.SysUser;
import com.pm.modules.auth.service.SysUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final SysUserService sysUserService;

    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping("/{id}")
    public Result<SysUser> get(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody SysUser user) {
        return Result.success(sysUserService.save(user));
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        return Result.success(sysUserService.updateById(user));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(sysUserService.removeById(id));
    }

    @GetMapping
    public Result<PageResult<SysUser>> list(@RequestParam(defaultValue = "1") long pageNo,
                                            @RequestParam(defaultValue = "10") long pageSize) {
        Page<SysUser> page = sysUserService.page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

