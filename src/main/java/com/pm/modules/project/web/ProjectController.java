package com.pm.modules.project.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.project.entity.Project;
import com.pm.modules.project.service.ProjectService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    public Result<Project> get(@PathVariable Long id) {
        return Result.success(projectService.getById(id));
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody Project project) {
        return Result.success(projectService.save(project));
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody Project project) {
        project.setId(id);
        return Result.success(projectService.updateById(project));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(projectService.removeById(id));
    }

    @GetMapping
    public Result<PageResult<Project>> list(@RequestParam(defaultValue = "1") long pageNo,
                                            @RequestParam(defaultValue = "10") long pageSize) {
        Page<Project> page = projectService.page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

