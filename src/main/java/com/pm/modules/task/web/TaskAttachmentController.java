package com.pm.modules.task.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.task.entity.TaskAttachment;
import com.pm.modules.task.service.TaskAttachmentService;
import org.springframework.web.bind.annotation.*;

/** 任务附件接口：新增、删除、按任务分页查询 */
@RestController
@RequestMapping("/api/task-attachments")
public class TaskAttachmentController {

    private final TaskAttachmentService taskAttachmentService;

    public TaskAttachmentController(TaskAttachmentService taskAttachmentService) {
        this.taskAttachmentService = taskAttachmentService;
    }

    /** 新增任务附件记录 */
    @PostMapping("/createTaskAttachment")
    public Result<Boolean> createTaskAttachment(@RequestBody TaskAttachment attachment) {
        return Result.success(taskAttachmentService.save(attachment));
    }

    /** 按 ID 删除附件记录（逻辑删除） */
    @DeleteMapping("/deleteTaskAttachment/{id}")
    public Result<Boolean> deleteTaskAttachment(@PathVariable Long id) {
        return Result.success(taskAttachmentService.removeById(id));
    }

    /** 按任务 ID 分页查询附件列表 */
    @GetMapping("/getTaskAttachmentList")
    public Result<PageResult<TaskAttachment>> getTaskAttachmentList(@RequestParam Long taskId,
                                                   @RequestParam(defaultValue = "1") long pageNo,
                                                   @RequestParam(defaultValue = "10") long pageSize) {
        Page<TaskAttachment> page = taskAttachmentService.lambdaQuery()
                .eq(TaskAttachment::getTaskId, taskId)
                .page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

