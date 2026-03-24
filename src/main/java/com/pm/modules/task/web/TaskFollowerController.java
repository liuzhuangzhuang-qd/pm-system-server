package com.pm.modules.task.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.task.entity.TaskFollower;
import com.pm.modules.task.service.TaskFollowerService;
import org.springframework.web.bind.annotation.*;

/** 任务关注人接口：新增、删除、按任务分页查询 */
@RestController
@RequestMapping("/api/task-followers")
public class TaskFollowerController {

    private final TaskFollowerService taskFollowerService;

    public TaskFollowerController(TaskFollowerService taskFollowerService) {
        this.taskFollowerService = taskFollowerService;
    }

    /** 新增任务关注人 */
    @PostMapping("/createTaskFollower")
    public Result<Boolean> createTaskFollower(@RequestBody TaskFollower follower) {
        return Result.success(taskFollowerService.save(follower));
    }

    /** 按 ID 取消关注（逻辑删除） */
    @DeleteMapping("/deleteTaskFollower/{id}")
    public Result<Boolean> deleteTaskFollower(@PathVariable Long id) {
        return Result.success(taskFollowerService.removeById(id));
    }

    /** 按任务 ID 分页查询关注人列表 */
    @GetMapping("/getTaskFollowerList")
    public Result<PageResult<TaskFollower>> getTaskFollowerList(@RequestParam Long taskId,
                                                 @RequestParam(defaultValue = "1") long pageNo,
                                                 @RequestParam(defaultValue = "10") long pageSize) {
        Page<TaskFollower> page = taskFollowerService.lambdaQuery()
                .eq(TaskFollower::getTaskId, taskId)
                .page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

