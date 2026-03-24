package com.pm.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.task.entity.Task;
import com.pm.modules.task.mapper.TaskMapper;
import com.pm.modules.task.service.TaskService;
import org.springframework.stereotype.Service;

/** 任务服务实现，基于 MyBatis-Plus 默认 CRUD */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
}

