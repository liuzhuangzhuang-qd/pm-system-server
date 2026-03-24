package com.pm.modules.project.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.result.PageResult;
import com.pm.common.result.Result;
import com.pm.modules.project.entity.ProjectMember;
import com.pm.modules.project.service.ProjectMemberService;
import org.springframework.web.bind.annotation.*;

/** 项目成员接口：添加、移除、按项目分页查询 */
@RestController
@RequestMapping("/api/project-members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    /** 添加项目成员 */
    @PostMapping("/createProjectMember")
    public Result<Boolean> createProjectMember(@RequestBody ProjectMember member) {
        return Result.success(projectMemberService.save(member));
    }

    /** 按 ID 移除项目成员 */
    @DeleteMapping("/deleteProjectMember/{id}")
    public Result<Boolean> deleteProjectMember(@PathVariable Long id) {
        return Result.success(projectMemberService.removeById(id));
    }

    /** 按项目 ID 分页查询成员列表 */
    @GetMapping("/getProjectMemberList")
    public Result<PageResult<ProjectMember>> getProjectMemberList(@RequestParam Long projectId,
                                                  @RequestParam(defaultValue = "1") long pageNo,
                                                  @RequestParam(defaultValue = "10") long pageSize) {
        Page<ProjectMember> page = projectMemberService.lambdaQuery()
                .eq(ProjectMember::getProjectId, projectId)
                .page(Page.of(pageNo, pageSize));
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }
}

