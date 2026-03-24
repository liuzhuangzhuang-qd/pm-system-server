package com.pm.common.exception;

import com.pm.common.result.ResultCode;
import lombok.Getter;

/** 业务异常，携带错误码与消息 */
@Getter
public class BizException extends RuntimeException {

    /** 错误码，对应 ResultCode */
    private final int code;

    /** 使用默认失败码，自定义消息 */
    public BizException(String message) {
        super(message);
        this.code = ResultCode.FAIL.getCode();
    }

    /** 使用指定 ResultCode 的 code 与 msg */
    public BizException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }
}

