package com.pm.modules.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("task")
public class Task extends BaseEntity {

    /** 任务标题 */
    private String title;
    /** 所属项目 ID */
    private Long projectId;
    /** 父任务 ID */
    private Long parentId;
    /** 优先级：1 低 2 中 3 高 */
    private Integer priority;
    /** 状态：todo/doing/done/closed */
    private String status;
    /** 指派人 ID */
    private Long assigneeId;
    /** 创建人 ID */
    private Long creatorId;
    /** 开始时间 */
    private LocalDateTime startTime;
    /** 结束时间 */
    private LocalDateTime endTime;
    /** 任务描述 */
    private String description;
}

