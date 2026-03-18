package com.pm.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.task.entity.TaskHistory;
import com.pm.modules.task.mapper.TaskHistoryMapper;
import com.pm.modules.task.service.TaskHistoryService;
import org.springframework.stereotype.Service;

@Service
public class TaskHistoryServiceImpl extends ServiceImpl<TaskHistoryMapper, TaskHistory> implements TaskHistoryService {
}

