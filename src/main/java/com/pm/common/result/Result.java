package com.pm.common.result;

import lombok.Data;

/** 统一 API 响应包装：code、msg、data */
@Data
public class Result<T> {

    /** 状态码（0 成功） */
    private int code;
    /** 提示信息 */
    private String msg;
    /** 业务数据 */
    private T data;

    /** 成功响应，无 data */
    public static <T> Result<T> success() {
        Result<T> r = new Result<>();
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMsg(ResultCode.SUCCESS.getMsg());
        return r;
    }

    /** 成功响应，带 data */
    public static <T> Result<T> success(T data) {
        Result<T> r = success();
        r.setData(data);
        return r;
    }

    /** 失败响应，使用默认失败码 */
    public static <T> Result<T> fail(String msg) {
        Result<T> r = new Result<>();
        r.setCode(ResultCode.FAIL.getCode());
        r.setMsg(msg);
        return r;
    }

    /** 失败响应，指定 code 与 msg */
    public static <T> Result<T> fail(int code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}

