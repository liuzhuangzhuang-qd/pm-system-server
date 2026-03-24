package com.pm.modules.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.auth.entity.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/** 操作日志表 Mapper */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {
}

