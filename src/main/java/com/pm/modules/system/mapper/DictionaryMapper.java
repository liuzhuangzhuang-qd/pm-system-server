package com.pm.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pm.modules.system.entity.Dictionary;
import org.apache.ibatis.annotations.Mapper;

/** 数据字典表 Mapper */
@Mapper
public interface DictionaryMapper extends BaseMapper<Dictionary> {
}
