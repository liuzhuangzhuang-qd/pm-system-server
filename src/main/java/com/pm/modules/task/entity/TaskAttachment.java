package com.pm.modules.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("task_attachment")
public class TaskAttachment extends BaseEntity {

    private Long taskId;
    private String fileName;
    private String filePath;
    private Long fileSize;
}

