package com.pm.modules.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.project.entity.ProjectMember;
import org.apache.ibatis.annotations.Mapper;

/** 项目成员表 Mapper */
@Mapper
public interface ProjectMemberMapper extends BaseMapper<ProjectMember> {
}

