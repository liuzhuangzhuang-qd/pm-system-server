package com.pm.modules.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.task.entity.TaskComment;
import org.apache.ibatis.annotations.Mapper;

/** 任务评论表 Mapper */
@Mapper
public interface TaskCommentMapper extends BaseMapper<TaskComment> {
}

