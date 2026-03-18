package com.pm.modules.auth.service.impl;

import com.pm.common.exception.BizException;
import com.pm.common.result.ResultCode;
import com.pm.common.util.JwtUtil;
import com.pm.modules.auth.entity.SysUser;
import com.pm.modules.auth.service.AuthService;
import com.pm.modules.auth.service.SysUserService;
import com.pm.modules.auth.web.dto.LoginRequest;
import com.pm.modules.auth.web.dto.LoginResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final SysUserService sysUserService;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(SysUserService sysUserService,
                           JwtUtil jwtUtil) {
        this.sysUserService = sysUserService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResult login(LoginRequest request) {
        String username = request.getUsername();
        SysUser user = sysUserService.getByUsername(username);
        if (user == null || user.getStatus() != 1) {
            log.warn("登录失败: 用户不存在或已禁用 username={}", username);
            throw new BizException("用户不存在或已禁用");
        }
        if (!request.getPassword().equals(user.getPassword())) {
            log.warn("登录失败: 密码错误 username={}", username);
            throw new BizException("用户名或密码错误");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        String token = jwtUtil.generateToken(user.getUsername(), claims);
        log.info("登录成功 username={} userId={}", username, user.getId());
        return new LoginResult(token);
    }

    @Override
    public LoginResult refresh(String token) {
        // 简化：直接基于旧 token 中的 subject 重新生成新 token
        String username = jwtUtil.parseToken(token).getSubject();
        log.debug("刷新 token username={}", username);
        Map<String, Object> claims = new HashMap<>();
        return new LoginResult(jwtUtil.generateToken(username, claims));
    }

    @Override
    public SysUser getCurrentUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new BizException(ResultCode.UNAUTHORIZED);
        }
        String username = auth.getName();
        SysUser user = sysUserService.getByUsername(username);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        user.setPassword(null);
        return user;
    }
}

