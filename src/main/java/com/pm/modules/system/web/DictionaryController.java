package com.pm.modules.system.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.system.entity.Dictionary;
import com.pm.modules.system.service.DictionaryService;
import org.springframework.web.bind.annotation.*;

/** 数据字典接口：CRUD 与按类型分页查询 */
@RestController
@RequestMapping("/api/dict")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    /** 新增字典项 */
    @PostMapping("/createDictionary")
    public Result<Boolean> createDictionary(@RequestBody Dictionary dict) {
        return Result.success(dictionaryService.save(dict));
    }

    /** 按 ID 更新字典项 */
    @PutMapping("/updateDictionary/{id}")
    public Result<Boolean> updateDictionary(@PathVariable Long id, @RequestBody Dictionary dict) {
        dict.setId(id);
        return Result.success(dictionaryService.updateById(dict));
    }

    /** 按 ID 获取字典项 */
    @GetMapping("/getDictionaryById/{id}")
    public Result<Dictionary> getDictionaryById(@PathVariable Long id) {
        return Result.success(dictionaryService.getById(id));
    }

    /** 按 ID 删除字典项（逻辑删除） */
    @DeleteMapping("/deleteDictionary/{id}")
    public Result<Boolean> deleteDictionary(@PathVariable Long id) {
        return Result.success(dictionaryService.removeById(id));
    }

    /** 分页查询字典列表，可按 dictType 过滤 */
    @GetMapping("/getDictionaryList")
    public Result<PageResult<Dictionary>> getDictionaryList(
            @RequestParam(required = false) String dictType,
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize) {
        Page<Dictionary> page = dictionaryService.lambdaQuery()
                .eq(dictType != null && !dictType.isBlank(), Dictionary::getDictType, dictType)
                .orderByAsc(Dictionary::getDictType)
                .orderByAsc(Dictionary::getSort)
                .page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}
