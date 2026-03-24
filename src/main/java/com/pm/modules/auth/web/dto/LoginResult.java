package com.pm.modules.auth.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResult {

    /** JWT 令牌 */
    private String token;
}

