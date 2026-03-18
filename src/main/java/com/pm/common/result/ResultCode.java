package com.pm.common.result;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(0, "success"),
    FAIL(1, "fail"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "forbidden"),
    NOT_FOUND(404, "not found"),
    SERVER_ERROR(500, "server error"),
    VALID_ERROR(400, "parameter error");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

