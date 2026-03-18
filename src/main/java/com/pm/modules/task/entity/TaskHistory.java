package com.pm.modules.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("task_history")
public class TaskHistory extends BaseEntity {

    private Long taskId;
    private Long userId;
    private String operate;
    private String content;
}

