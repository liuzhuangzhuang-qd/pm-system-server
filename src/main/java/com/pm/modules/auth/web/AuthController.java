package com.pm.modules.auth.web;

import com.pm.common.result.Result;
import com.pm.modules.auth.entity.SysUser;
import com.pm.modules.auth.service.AuthService;
import com.pm.modules.auth.web.dto.LoginRequest;
import com.pm.modules.auth.web.dto.LoginResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/** 认证相关接口：登录、刷新、登出、当前用户信息 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** 登录，校验账号密码并返回 JWT */
    @PostMapping("/createLoginToken")
    public Result<LoginResult> createLoginToken(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.createLoginToken(request));
    }

    /** 刷新 token，根据旧 token 签发新 token */
    @PostMapping("/updateLoginToken")
    public Result<LoginResult> updateLoginToken(@RequestParam String token) {
        return Result.success(authService.updateLoginToken(token));
    }

    /** 登出（前端删 token；后端预留扩展如黑名单） */
    @PostMapping("/deleteLoginToken")
    public Result<Void> deleteLoginToken() {
        return Result.success();
    }

    /** 获取当前登录用户信息（需 JWT，password 不返回） */
    @GetMapping("/getUserInfo")
    public Result<SysUser> getUserInfo() {
        return Result.success(authService.getCurrentUserInfo());
    }
}

