package com.pm.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.task.entity.TaskAttachment;
import com.pm.modules.task.mapper.TaskAttachmentMapper;
import com.pm.modules.task.service.TaskAttachmentService;
import org.springframework.stereotype.Service;

@Service
public class TaskAttachmentServiceImpl extends ServiceImpl<TaskAttachmentMapper, TaskAttachment> implements TaskAttachmentService {
}

