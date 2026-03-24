package com.pm.modules.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.project.entity.ProjectMember;
import com.pm.modules.project.mapper.ProjectMemberMapper;
import com.pm.modules.project.service.ProjectMemberService;
import org.springframework.stereotype.Service;

/** 项目成员服务实现，基于 MyBatis-Plus 默认 CRUD */
@Service
public class ProjectMemberServiceImpl extends ServiceImpl<ProjectMemberMapper, ProjectMember> implements ProjectMemberService {
}

