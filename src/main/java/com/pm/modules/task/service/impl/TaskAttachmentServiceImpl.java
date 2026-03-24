package com.pm.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.task.entity.TaskAttachment;
import com.pm.modules.task.mapper.TaskAttachmentMapper;
import com.pm.modules.task.service.TaskAttachmentService;
import org.springframework.stereotype.Service;

/** 任务附件服务实现，基于 MyBatis-Plus 默认 CRUD */
@Service
public class TaskAttachmentServiceImpl extends ServiceImpl<TaskAttachmentMapper, TaskAttachment> implements TaskAttachmentService {
}

