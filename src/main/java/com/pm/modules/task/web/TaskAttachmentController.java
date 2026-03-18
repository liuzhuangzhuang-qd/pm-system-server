package com.pm.modules.task.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.task.entity.TaskAttachment;
import com.pm.modules.task.service.TaskAttachmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task-attachments")
public class TaskAttachmentController {

    private final TaskAttachmentService taskAttachmentService;

    public TaskAttachmentController(TaskAttachmentService taskAttachmentService) {
        this.taskAttachmentService = taskAttachmentService;
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody TaskAttachment attachment) {
        return Result.success(taskAttachmentService.save(attachment));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(taskAttachmentService.removeById(id));
    }

    @GetMapping
    public Result<PageResult<TaskAttachment>> list(@RequestParam Long taskId,
                                                   @RequestParam(defaultValue = "1") long pageNo,
                                                   @RequestParam(defaultValue = "10") long pageSize) {
        Page<TaskAttachment> page = taskAttachmentService.lambdaQuery()
                .eq(TaskAttachment::getTaskId, taskId)
                .page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

