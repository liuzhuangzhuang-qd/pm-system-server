package com.pm.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/** 实体基类：id、createTime、updateTime、deleted */
@Data
public class BaseEntity {

    /** 主键 */
    @TableId
    private Long id;
    /** 创建时间（插入时填充） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 更新时间（插入/更新时填充） */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /** 逻辑删除：0 未删除 1 已删除 */
    @TableLogic
    private Integer deleted;
}

