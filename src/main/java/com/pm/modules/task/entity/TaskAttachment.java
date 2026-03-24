package com.pm.modules.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pm.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("task_attachment")
public class TaskAttachment extends BaseEntity {

    /** 任务 ID */
    private Long taskId;
    /** 文件名 */
    private String fileName;
    /** 文件路径 */
    private String filePath;
    /** 文件大小（字节） */
    private Long fileSize;
}

