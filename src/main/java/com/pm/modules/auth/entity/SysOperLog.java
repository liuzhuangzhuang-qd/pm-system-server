package com.pm.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_oper_log")
public class SysOperLog extends BaseEntity {

    private Long userId;
    private String module;
    private String content;
    private String ip;
    private java.time.LocalDateTime operTime;
}

