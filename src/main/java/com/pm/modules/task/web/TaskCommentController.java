package com.pm.modules.task.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.task.entity.TaskComment;
import com.pm.modules.task.service.TaskCommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task-comments")
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    public TaskCommentController(TaskCommentService taskCommentService) {
        this.taskCommentService = taskCommentService;
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody TaskComment comment) {
        return Result.success(taskCommentService.save(comment));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(taskCommentService.removeById(id));
    }

    @GetMapping
    public Result<PageResult<TaskComment>> list(@RequestParam Long taskId,
                                                @RequestParam(defaultValue = "1") long pageNo,
                                                @RequestParam(defaultValue = "10") long pageSize) {
        Page<TaskComment> page = taskCommentService.lambdaQuery()
                .eq(TaskComment::getTaskId, taskId)
                .page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

