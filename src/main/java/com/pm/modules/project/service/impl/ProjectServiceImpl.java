package com.pm.modules.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.project.entity.Project;
import com.pm.modules.project.mapper.ProjectMapper;
import com.pm.modules.project.service.ProjectService;
import org.springframework.stereotype.Service;

/** 项目服务实现，基于 MyBatis-Plus 默认 CRUD */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
}

