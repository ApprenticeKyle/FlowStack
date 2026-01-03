package org.r2learning.project.interfaces.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.r2learning.common.domain.common.Result;
import org.r2learning.common.utils.UserContextHolder;
import org.r2learning.project.application.cmd.AddProjectMembersCmd;
import org.r2learning.project.application.cmd.CloneProjectCmd;
import org.r2learning.project.application.cmd.CreateProjectCmd;
import org.r2learning.project.application.cmd.ListProjectCmd;
import org.r2learning.project.application.cmd.UpdateProjectCmd;
import org.r2learning.project.application.service.ProjectApplicationService;
import org.r2learning.project.interfaces.web.dto.ProjectDTO;
import org.r2learning.project.interfaces.web.dto.ProjectMemberDTO;
import org.r2learning.project.interfaces.web.dto.ProjectStatsDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectApplicationService projectApplicationService;

    @GetMapping
    public Result<List<ProjectDTO>> list(ListProjectCmd cmd) {
        return Result.success(projectApplicationService.list(cmd));
    }

    @PostMapping
    public Result<Long> create(@RequestBody CreateProjectCmd cmd) {
        Long ownerId = UserContextHolder.getCurrentUserId();
        cmd.setOwnerId(ownerId);
        Long projectId = projectApplicationService.create(cmd);
        return Result.success(projectId);
    }

    @GetMapping("/{id}")
    public Result<ProjectDTO> get(@PathVariable Long id) {
        return Result.success(projectApplicationService.get(id));
    }

    @PutMapping("/{id}")
    public Result<ProjectDTO> update(@PathVariable Long id, @RequestBody UpdateProjectCmd cmd) {
        cmd.setId(id);
        Long ownerId = UserContextHolder.getCurrentUserId();
        cmd.setOwnerId(ownerId);
        ProjectDTO project = projectApplicationService.update(cmd);
        return Result.success(project);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        projectApplicationService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}/stats")
    public Result<ProjectStatsDTO> getProjectStats(@PathVariable Long id) {
        return Result.success(projectApplicationService.getProjectStats(id));
    }

    // 项目成员管理API
    @PostMapping("/{id}/members")
    public Result<Void> addProjectMembers(@PathVariable Long id, @RequestBody AddProjectMembersCmd cmd) {
        cmd.setProjectId(id);
        projectApplicationService.addProjectMembers(cmd);
        return Result.success();
    }

    @DeleteMapping("/{id}/members/{userId}")
    public Result<Void> removeProjectMember(@PathVariable Long id, @PathVariable Long userId) {
        projectApplicationService.removeProjectMember(id, userId);
        return Result.success();
    }

    @PutMapping("/{id}/members/{userId}/role")
    public Result<Void> updateProjectMemberRole(
        @PathVariable Long id,
        @PathVariable Long userId,
        @RequestParam String role) {
        projectApplicationService.updateProjectMemberRole(id, userId, role);
        return Result.success();
    }

    @GetMapping("/users/search")
    public Result<List<ProjectMemberDTO>> searchUsers(@RequestParam String keyword) {
        return Result.success(projectApplicationService.searchUsers(keyword));
    }

    // 项目收藏/星标
    @PostMapping("/{id}/star")
    public Result<ProjectDTO> toggleStar(@PathVariable Long id) {
        return Result.success(projectApplicationService.toggleStar(id));
    }

    // 项目归档
    @PostMapping("/{id}/archive")
    public Result<ProjectDTO> archive(@PathVariable Long id) {
        return Result.success(projectApplicationService.archive(id));
    }

    // 取消归档
    @PostMapping("/{id}/unarchive")
    public Result<ProjectDTO> unarchive(@PathVariable Long id) {
        return Result.success(projectApplicationService.unarchive(id));
    }

    // 复制/克隆项目
    @PostMapping("/{id}/clone")
    public Result<ProjectDTO> cloneProject(@PathVariable Long id, @RequestBody CloneProjectCmd cmd) {
        cmd.setSourceProjectId(id);
        if (cmd.getNewName() == null || cmd.getNewName().trim().isEmpty()) {
            // 如果没有提供新名称，使用默认名称
            ProjectDTO sourceProject = projectApplicationService.get(id);
            cmd.setNewName(sourceProject.getName() + " (Copy)");
        }
        return Result.success(projectApplicationService.cloneProject(cmd));
    }
}
