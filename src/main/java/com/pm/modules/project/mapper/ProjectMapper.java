package com.pm.modules.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.project.entity.Project;
import org.apache.ibatis.annotations.Mapper;

/** 项目表 Mapper */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
}

