package com.pm.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("system_dict")
public class Dictionary extends BaseEntity {

    /** 字典类型（如 task_status） */
    private String dictType;
    /** 字典标签（展示用） */
    private String dictLabel;
    /** 字典值（存储/代码用） */
    private String dictValue;
    /** 排序，升序 */
    private Integer sort;
}
