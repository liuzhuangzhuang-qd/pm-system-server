package com.pm.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.task.entity.TaskFollower;
import com.pm.modules.task.mapper.TaskFollowerMapper;
import com.pm.modules.task.service.TaskFollowerService;
import org.springframework.stereotype.Service;

@Service
public class TaskFollowerServiceImpl extends ServiceImpl<TaskFollowerMapper, TaskFollower> implements TaskFollowerService {
}

