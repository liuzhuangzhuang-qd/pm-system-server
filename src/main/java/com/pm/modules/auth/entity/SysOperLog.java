package com.pm.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_oper_log")
public class SysOperLog extends BaseEntity {

    /** 操作用户 ID */
    private Long userId;
    /** 模块名 */
    private String module;
    /** 操作内容 */
    private String content;
    /** 操作 IP */
    private String ip;
    /** 操作时间 */
    private java.time.LocalDateTime operTime;
}

