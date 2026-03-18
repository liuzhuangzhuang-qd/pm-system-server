package com.pm.modules.project.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.project.entity.ProjectMember;
import com.pm.modules.project.service.ProjectMemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project-members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody ProjectMember member) {
        return Result.success(projectMemberService.save(member));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> remove(@PathVariable Long id) {
        return Result.success(projectMemberService.removeById(id));
    }

    @GetMapping
    public Result<PageResult<ProjectMember>> list(@RequestParam Long projectId,
                                                  @RequestParam(defaultValue = "1") long pageNo,
                                                  @RequestParam(defaultValue = "10") long pageSize) {
        Page<ProjectMember> page = projectMemberService.lambdaQuery()
                .eq(ProjectMember::getProjectId, projectId)
                .page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

