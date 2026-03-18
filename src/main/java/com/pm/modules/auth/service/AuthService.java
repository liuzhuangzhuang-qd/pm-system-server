package com.pm.modules.auth.service;

import com.pm.modules.auth.entity.SysUser;
import com.pm.modules.auth.web.dto.LoginRequest;
import com.pm.modules.auth.web.dto.LoginResult;

public interface AuthService {

    LoginResult login(LoginRequest request);

    LoginResult refresh(String token);

    /** 当前登录用户资料（不含密码），需已携带有效 JWT */
    SysUser getCurrentUserProfile();
}

