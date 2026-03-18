package com.pm.modules.task.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.task.entity.TaskHistory;
import com.pm.modules.task.service.TaskHistoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task-history")
public class TaskHistoryController {

    private final TaskHistoryService taskHistoryService;

    public TaskHistoryController(TaskHistoryService taskHistoryService) {
        this.taskHistoryService = taskHistoryService;
    }

    @GetMapping
    public Result<PageResult<TaskHistory>> list(@RequestParam Long taskId,
                                                @RequestParam(defaultValue = "1") long pageNo,
                                                @RequestParam(defaultValue = "10") long pageSize) {
        Page<TaskHistory> page = taskHistoryService.lambdaQuery()
                .eq(TaskHistory::getTaskId, taskId)
                .orderByDesc(TaskHistory::getCreateTime)
                .page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

