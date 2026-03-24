package com.pm.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.task.entity.TaskFollower;
import com.pm.modules.task.mapper.TaskFollowerMapper;
import com.pm.modules.task.service.TaskFollowerService;
import org.springframework.stereotype.Service;

/** 任务关注人服务实现，基于 MyBatis-Plus 默认 CRUD */
@Service
public class TaskFollowerServiceImpl extends ServiceImpl<TaskFollowerMapper, TaskFollower> implements TaskFollowerService {
}

