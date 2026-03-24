package com.pm.modules.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.task.entity.Task;
import org.apache.ibatis.annotations.Mapper;

/** 任务表 Mapper */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {
}

