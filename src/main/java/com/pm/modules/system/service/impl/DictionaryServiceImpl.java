package com.pm.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pm.modules.system.entity.Dictionary;
import com.pm.modules.system.mapper.DictionaryMapper;
import com.pm.modules.system.service.DictionaryService;
import org.springframework.stereotype.Service;

/** 数据字典服务实现，基于 MyBatis-Plus 默认 CRUD */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {
}
