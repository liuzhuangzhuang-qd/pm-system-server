package com.pm.modules.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("task_follower")
public class TaskFollower extends BaseEntity {

    /** 任务 ID */
    private Long taskId;
    /** 关注人用户 ID */
    private Long userId;
}

