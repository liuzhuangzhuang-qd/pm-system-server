package com.pm.modules.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("task_comment")
public class TaskComment extends BaseEntity {

    /** 任务 ID */
    private Long taskId;
    /** 评论人用户 ID */
    private Long userId;
    /** 评论内容 */
    private String content;
}

