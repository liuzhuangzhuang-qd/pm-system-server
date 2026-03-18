package com.pm.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.task.entity.TaskComment;
import com.pm.modules.task.mapper.TaskCommentMapper;
import com.pm.modules.task.service.TaskCommentService;
import org.springframework.stereotype.Service;

@Service
public class TaskCommentServiceImpl extends ServiceImpl<TaskCommentMapper, TaskComment> implements TaskCommentService {
}

