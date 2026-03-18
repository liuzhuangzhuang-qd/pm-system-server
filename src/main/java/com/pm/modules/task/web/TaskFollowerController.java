package com.pm.modules.task.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.task.entity.TaskFollower;
import com.pm.modules.task.service.TaskFollowerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task-followers")
public class TaskFollowerController {

    private final TaskFollowerService taskFollowerService;

    public TaskFollowerController(TaskFollowerService taskFollowerService) {
        this.taskFollowerService = taskFollowerService;
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody TaskFollower follower) {
        return Result.success(taskFollowerService.save(follower));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(taskFollowerService.removeById(id));
    }

    @GetMapping
    public Result<PageResult<TaskFollower>> list(@RequestParam Long taskId,
                                                 @RequestParam(defaultValue = "1") long pageNo,
                                                 @RequestParam(defaultValue = "10") long pageSize) {
        Page<TaskFollower> page = taskFollowerService.lambdaQuery()
                .eq(TaskFollower::getTaskId, taskId)
                .page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

