package com.pm.modules.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.task.entity.TaskAttachment;
import org.apache.ibatis.annotations.Mapper;

/** 任务附件表 Mapper */
@Mapper
public interface TaskAttachmentMapper extends BaseMapper<TaskAttachment> {
}

