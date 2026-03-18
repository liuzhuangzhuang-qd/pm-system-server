package com.pm.modules.project.web;

import com.pm.common.result.Result;
import com.pm.modules.project.entity.ProjectConfig;
import com.pm.modules.project.service.ProjectConfigService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project-config")
public class ProjectConfigController {

    private final ProjectConfigService projectConfigService;

    public ProjectConfigController(ProjectConfigService projectConfigService) {
        this.projectConfigService = projectConfigService;
    }

    @GetMapping("/{projectId}")
    public Result<ProjectConfig> getByProject(@PathVariable Long projectId) {
        ProjectConfig config = projectConfigService.lambdaQuery()
                .eq(ProjectConfig::getProjectId, projectId)
                .one();
        return Result.success(config);
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody ProjectConfig config) {
        ProjectConfig existing = projectConfigService.lambdaQuery()
                .eq(ProjectConfig::getProjectId, config.getProjectId())
                .one();
        if (existing != null) {
            config.setId(existing.getId());
        }
        return Result.success(projectConfigService.saveOrUpdate(config));
    }
}

