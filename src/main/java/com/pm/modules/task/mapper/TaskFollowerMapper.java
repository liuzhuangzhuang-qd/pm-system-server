package com.pm.modules.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.task.entity.TaskFollower;
import org.apache.ibatis.annotations.Mapper;

/** 任务关注人表 Mapper */
@Mapper
public interface TaskFollowerMapper extends BaseMapper<TaskFollower> {
}

