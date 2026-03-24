package com.pm.modules.project.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.project.entity.Project;
import com.pm.modules.project.service.ProjectService;
import org.springframework.web.bind.annotation.*;

/** 项目管理接口：CRUD 与分页列表 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /** 按 ID 获取项目 */
    @GetMapping("/getProjectById/{id}")
    public Result<Project> getProjectById(@PathVariable Long id) {
        return Result.success(projectService.getById(id));
    }

    /** 新增项目 */
    @PostMapping("/createProject")
    public Result<Boolean> createProject(@RequestBody Project project) {
        return Result.success(projectService.save(project));
    }

    /** 按 ID 更新项目 */
    @PutMapping("/updateProject/{id}")
    public Result<Boolean> updateProject(@PathVariable Long id, @RequestBody Project project) {
        project.setId(id);
        return Result.success(projectService.updateById(project));
    }

    /** 按 ID 删除项目（逻辑删除） */
    @DeleteMapping("/deleteProject/{id}")
    public Result<Boolean> deleteProject(@PathVariable Long id) {
        return Result.success(projectService.removeById(id));
    }

    /** 分页查询项目列表 */
    @GetMapping("/getProjectList")
    public Result<PageResult<Project>> getProjectList(@RequestParam(defaultValue = "1") long pageNo,
                                            @RequestParam(defaultValue = "10") long pageSize) {
        Page<Project> page = projectService.page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

