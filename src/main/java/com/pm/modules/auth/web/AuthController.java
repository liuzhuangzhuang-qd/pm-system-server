package com.pm.modules.auth.web;

import com.pm.common.result.Result;
import com.pm.modules.auth.entity.SysUser;
import com.pm.modules.auth.service.AuthService;
import com.pm.modules.auth.web.dto.LoginRequest;
import com.pm.modules.auth.web.dto.LoginResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginResult> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @PostMapping("/refresh")
    public Result<LoginResult> refresh(@RequestParam String token) {
        return Result.success(authService.refresh(token));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        // 前端删除 token 即可，后端此处预留扩展（如黑名单）
        return Result.success();
    }

    @GetMapping("/user-info")
    public Result<SysUser> userInfo() {
        return Result.success(authService.getCurrentUserProfile());
    }
}

