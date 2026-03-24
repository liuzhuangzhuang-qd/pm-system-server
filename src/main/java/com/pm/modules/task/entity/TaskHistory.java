package com.pm.modules.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("task_history")
public class TaskHistory extends BaseEntity {

    /** 任务 ID */
    private Long taskId;
    /** 操作人用户 ID */
    private Long userId;
    /** 操作类型 */
    private String operate;
    /** 变更内容描述 */
    private String content;
}

