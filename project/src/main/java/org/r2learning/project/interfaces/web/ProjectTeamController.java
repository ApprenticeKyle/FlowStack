package org.r2learning.project.interfaces.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.r2learning.common.domain.common.Result;
import org.r2learning.project.application.cmd.CheckTeamRemovalImpactCmd;
import org.r2learning.project.application.cmd.GetUsersByTeamIdsCmd;
import org.r2learning.project.application.service.ProjectApplicationService;
import org.r2learning.project.interfaces.web.dto.ProjectMemberDTO;
import org.r2learning.project.interfaces.web.dto.TeamDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目团队相关API
 * 处理项目与团队、团队成员的关联查询
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectTeamController {

    private final ProjectApplicationService projectApplicationService;

    /**
     * 获取所有可用团队列表
     */
    @GetMapping("/teams/available")
    public Result<List<TeamDTO>> getAvailableTeams() {
        return Result.success(projectApplicationService.getAvailableTeams());
    }

    /**
     * 获取指定团队下的所有成员
     */
    @GetMapping("/teams/{teamId}/members")
    public Result<List<ProjectMemberDTO>> getTeamMembers(@PathVariable Long teamId) {
        return Result.success(projectApplicationService.getTeamMembers(teamId));
    }

    /**
     * 根据团队ID列表查询可用用户（用于项目添加成员）
     * 使用POST方式，参数放在body中，更灵活且符合RESTful最佳实践
     */
    @PostMapping("/members/available")
    public Result<List<ProjectMemberDTO>> getAvailableMembers(@RequestBody GetUsersByTeamIdsCmd cmd) {
        return Result.success(projectApplicationService.getUsersByTeamIds(cmd.getTeamIds()));
    }

    /**
     * 检查移除团队对项目成员的影响
     * 用于前端在更新项目前提示用户
     */
    @PostMapping("/{id}/teams/removal-impact")
    public Result<List<ProjectMemberDTO>> checkTeamRemovalImpact(
        @PathVariable Long id,
        @RequestBody CheckTeamRemovalImpactCmd cmd) {
        cmd.setProjectId(id);
        return Result.success(projectApplicationService.checkTeamRemovalImpact(id, cmd.getNewTeamIds()));
    }
}

