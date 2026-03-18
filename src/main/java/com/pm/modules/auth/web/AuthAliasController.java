package com.pm.modules.auth.web;

import com.pm.common.result.Result;
import com.pm.modules.auth.entity.SysUser;
import com.pm.modules.auth.service.AuthService;
import com.pm.modules.auth.web.dto.LoginRequest;
import com.pm.modules.auth.web.dto.LoginResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 与常见前端约定一致的无 /api 前缀路径，避免误调 /auth/login 被 Security 拦截为 403。
 */
@RestController
@RequestMapping("/auth")
public class AuthAliasController {

    private final AuthService authService;

    public AuthAliasController(AuthService authService) {
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

    @GetMapping("/user-info")
    public Result<SysUser> userInfo() {
        return Result.success(authService.getCurrentUserProfile());
    }
}
