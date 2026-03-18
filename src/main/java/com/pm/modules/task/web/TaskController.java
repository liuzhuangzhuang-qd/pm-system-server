package com.pm.modules.task.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.task.entity.Task;
import com.pm.modules.task.service.TaskService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public Result<Task> get(@PathVariable Long id) {
        return Result.success(taskService.getById(id));
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody Task task) {
        return Result.success(taskService.save(task));
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody Task task) {
        task.setId(id);
        return Result.success(taskService.updateById(task));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(taskService.removeById(id));
    }

    @GetMapping
    public Result<PageResult<Task>> list(@RequestParam(required = false) Long projectId,
                                         @RequestParam(defaultValue = "1") long pageNo,
                                         @RequestParam(defaultValue = "10") long pageSize) {
        Page<Task> page = taskService.lambdaQuery()
                .eq(projectId != null, Task::getProjectId, projectId)
                .page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

