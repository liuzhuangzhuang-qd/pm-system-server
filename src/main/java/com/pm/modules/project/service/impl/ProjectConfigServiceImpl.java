package com.pm.modules.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.project.entity.ProjectConfig;
import com.pm.modules.project.mapper.ProjectConfigMapper;
import com.pm.modules.project.service.ProjectConfigService;
import org.springframework.stereotype.Service;

@Service
public class ProjectConfigServiceImpl extends ServiceImpl<ProjectConfigMapper, ProjectConfig> implements ProjectConfigService {
}

