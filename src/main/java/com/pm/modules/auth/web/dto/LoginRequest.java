package com.pm.modules.auth.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    /** 登录账号（必填） */
    @NotBlank
    private String username;
    /** 密码（必填，明文） */
    @NotBlank
    private String password;
}

