package com.pm.modules.auth.service;

import com.pm.modules.auth.entity.SysUser;
import com.pm.modules.auth.web.dto.LoginRequest;
import com.pm.modules.auth.web.dto.LoginResult;

/** 认证服务：登录、刷新 token、当前用户信息 */
public interface AuthService {

    /** 校验账号密码并返回 JWT */
    LoginResult createLoginToken(LoginRequest request);

    /** 根据旧 token 签发新 token */
    LoginResult updateLoginToken(String token);

    /** 当前登录用户资料（不含密码），需已携带有效 JWT */
    SysUser getCurrentUserInfo();
}

