package com.pm.modules.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.project.entity.ProjectConfig;
import org.apache.ibatis.annotations.Mapper;

/** 项目配置表 Mapper */
@Mapper
public interface ProjectConfigMapper extends BaseMapper<ProjectConfig> {
}

