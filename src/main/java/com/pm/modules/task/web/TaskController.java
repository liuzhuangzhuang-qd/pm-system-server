package com.pm.modules.task.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.task.entity.Task;
import com.pm.modules.task.service.TaskService;
import org.springframework.web.bind.annotation.*;

/** 任务管理接口：CRUD 与分页列表（可按项目过滤） */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /** 按 ID 获取任务 */
    @GetMapping("/getTaskById/{id}")
    public Result<Task> getTaskById(@PathVariable Long id) {
        return Result.success(taskService.getById(id));
    }

    /** 新增任务 */
    @PostMapping("/createTask")
    public Result<Boolean> createTask(@RequestBody Task task) {
        return Result.success(taskService.save(task));
    }

    /** 按 ID 更新任务 */
    @PutMapping("/updateTask/{id}")
    public Result<Boolean> updateTask(@PathVariable Long id, @RequestBody Task task) {
        task.setId(id);
        return Result.success(taskService.updateById(task));
    }

    /** 按 ID 删除任务（逻辑删除） */
    @DeleteMapping("/deleteTask/{id}")
    public Result<Boolean> deleteTask(@PathVariable Long id) {
        return Result.success(taskService.removeById(id));
    }

    /** 分页查询任务列表，可选按 projectId 过滤 */
    @GetMapping("/getTaskList")
    public Result<PageResult<Task>> getTaskList(@RequestParam(required = false) Long projectId,
                                         @RequestParam(defaultValue = "1") long pageNo,
                                         @RequestParam(defaultValue = "10") long pageSize) {
        Page<Task> page = taskService.lambdaQuery()
                .eq(projectId != null, Task::getProjectId, projectId)
                .page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

