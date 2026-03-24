package com.pm.modules.task.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.task.entity.TaskComment;
import com.pm.modules.task.service.TaskCommentService;
import org.springframework.web.bind.annotation.*;

/** 任务评论接口：新增、删除、按任务分页查询 */
@RestController
@RequestMapping("/api/task-comments")
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    public TaskCommentController(TaskCommentService taskCommentService) {
        this.taskCommentService = taskCommentService;
    }

    /** 新增任务评论 */
    @PostMapping("/createTaskComment")
    public Result<Boolean> createTaskComment(@RequestBody TaskComment comment) {
        return Result.success(taskCommentService.save(comment));
    }

    /** 按 ID 删除评论（逻辑删除） */
    @DeleteMapping("/deleteTaskComment/{id}")
    public Result<Boolean> deleteTaskComment(@PathVariable Long id) {
        return Result.success(taskCommentService.removeById(id));
    }

    /** 按任务 ID 分页查询评论列表 */
    @GetMapping("/getTaskCommentList")
    public Result<PageResult<TaskComment>> getTaskCommentList(@RequestParam Long taskId,
                                                @RequestParam(defaultValue = "1") long pageNo,
                                                @RequestParam(defaultValue = "10") long pageSize) {
        Page<TaskComment> page = taskCommentService.lambdaQuery()
                .eq(TaskComment::getTaskId, taskId)
                .page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

