package com.pm.modules.auth.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.auth.entity.SysUser;
import com.pm.modules.auth.service.SysUserService;
import org.springframework.web.bind.annotation.*;

/** 用户管理接口：CRUD 与分页列表 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final SysUserService sysUserService;

    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    /** 按 ID 获取用户 */
    @GetMapping("/getUserById/{id}")
    public Result<SysUser> getUserById(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }

    /** 新增用户 */
    @PostMapping("/createUser")
    public Result<Boolean> createUser(@RequestBody SysUser user) {
        return Result.success(sysUserService.save(user));
    }

    /** 按 ID 更新用户 */
    @PutMapping("/updateUser/{id}")
    public Result<Boolean> updateUser(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        return Result.success(sysUserService.updateById(user));
    }

    /** 按 ID 删除用户（逻辑删除） */
    @DeleteMapping("/deleteUser/{id}")
    public Result<Boolean> deleteUser(@PathVariable Long id) {
        return Result.success(sysUserService.removeById(id));
    }

    /** 分页查询用户列表 */
    @GetMapping("/getUserList")
    public Result<PageResult<SysUser>> getUserList(@RequestParam(defaultValue = "1") long pageNo,
                                            @RequestParam(defaultValue = "10") long pageSize) {
        Page<SysUser> page = sysUserService.page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

