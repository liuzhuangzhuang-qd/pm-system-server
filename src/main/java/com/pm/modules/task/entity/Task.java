package com.pm.modules.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("task")
public class Task extends BaseEntity {

    private String title;
    private Long projectId;
    private Long parentId;
    /**
     * 1低2中3高
     */
    private Integer priority;
    /**
     * todo/doing/done/closed
     */
    private String status;
    private Long assigneeId;
    private Long creatorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
}

