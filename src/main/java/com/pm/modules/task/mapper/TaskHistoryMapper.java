package com.pm.modules.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.task.entity.TaskHistory;
import org.apache.ibatis.annotations.Mapper;

/** 任务变更历史表 Mapper */
@Mapper
public interface TaskHistoryMapper extends BaseMapper<TaskHistory> {
}

