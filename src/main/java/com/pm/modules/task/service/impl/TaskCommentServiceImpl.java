package com.pm.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.task.entity.TaskComment;
import com.pm.modules.task.mapper.TaskCommentMapper;
import com.pm.modules.task.service.TaskCommentService;
import org.springframework.stereotype.Service;

/** 任务评论服务实现，基于 MyBatis-Plus 默认 CRUD */
@Service
public class TaskCommentServiceImpl extends ServiceImpl<TaskCommentMapper, TaskComment> implements TaskCommentService {
}

