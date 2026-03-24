package com.pm.modules.project.web;

import com.pm.common.result.Result;
import com.pm.modules.project.entity.ProjectConfig;
import com.pm.modules.project.service.ProjectConfigService;
import org.springframework.web.bind.annotation.*;

/** 项目配置接口：按项目查询/保存配置 */
@RestController
@RequestMapping("/api/project-config")
public class ProjectConfigController {

    private final ProjectConfigService projectConfigService;

    public ProjectConfigController(ProjectConfigService projectConfigService) {
        this.projectConfigService = projectConfigService;
    }

    /** 按项目 ID 获取配置（单条） */
    @GetMapping("/getProjectConfig/{projectId}")
    public Result<ProjectConfig> getProjectConfig(@PathVariable Long projectId) {
        ProjectConfig config = projectConfigService.lambdaQuery()
                .eq(ProjectConfig::getProjectId, projectId)
                .one();
        return Result.success(config);
    }

    /** 保存或更新项目配置（有则更新，无则新增） */
    @PostMapping("/saveProjectConfig")
    public Result<Boolean> saveProjectConfig(@RequestBody ProjectConfig config) {
        ProjectConfig existing = projectConfigService.lambdaQuery()
                .eq(ProjectConfig::getProjectId, config.getProjectId())
                .one();
        if (existing != null) {
            config.setId(existing.getId());
        }
        return Result.success(projectConfigService.saveOrUpdate(config));
    }
}

