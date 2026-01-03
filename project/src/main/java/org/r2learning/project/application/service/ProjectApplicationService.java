package org.r2learning.project.application.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.r2learning.common.domain.common.Result;
import org.r2learning.project.application.cmd.AddProjectMembersCmd;
import org.r2learning.project.application.cmd.CloneProjectCmd;
import org.r2learning.project.application.cmd.CreateProjectCmd;
import org.r2learning.project.application.cmd.ListProjectCmd;
import org.r2learning.project.application.cmd.UpdateProjectCmd;
import org.r2learning.project.application.mapper.ProjectMapper;
import org.r2learning.project.client.TaskFeignClient;
import org.r2learning.project.client.UserFeignClient;
import org.r2learning.project.domain.project.Project;
import org.r2learning.project.domain.project.gateway.ProjectGateway;
import org.r2learning.project.infrastructure.service.ProjectTeamService;
import org.r2learning.project.infrastructure.service.ProjectTagService;
import org.r2learning.project.interfaces.web.dto.ProjectDTO;
import org.r2learning.project.interfaces.web.dto.ProjectMemberDTO;
import org.r2learning.project.interfaces.web.dto.ProjectStatsDTO;
import org.r2learning.project.client.dto.TaskRemoteDTO;
import org.r2learning.project.client.dto.TeamRemoteDTO;
import org.r2learning.project.client.dto.UserRemoteDTO;
import org.r2learning.project.interfaces.web.dto.TeamDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectApplicationService {

    private final ProjectGateway projectGateway;
    private final TaskFeignClient taskFeignClient;
    private final UserFeignClient userFeignClient;
    private final ProjectTeamService projectTeamService;
    private final ProjectTagService projectTagService;

    @Transactional
    public Long create(CreateProjectCmd cmd) {
        Project project = Project.create(cmd.getName(), cmd.getDescription(), cmd.getOwnerId(),
            cmd.getDeadline(), cmd.getStartDate(), cmd.getPriority(), cmd.getCoverImage());
        Long projectId = projectGateway.save(project).getId();
        
        // 保存项目-团队关联
        if (cmd.getTeamIds() != null && !cmd.getTeamIds().isEmpty()) {
            projectTeamService.saveProjectTeams(projectId, cmd.getTeamIds());
        }
        
        // 保存项目标签
        if (cmd.getTags() != null && !cmd.getTags().isEmpty()) {
            projectTagService.saveProjectTags(projectId, cmd.getTags());
        }
        
        return projectId;
    }

    public List<ProjectDTO> list(ListProjectCmd cmd) {
        List<Project> projects = projectGateway.findByCriteria(cmd);
        List<ProjectDTO> projectDTOs = projects.stream()
            .map(project -> {
                ProjectDTO dto = ProjectMapper.INSTANCE.toDTO(project);
                // 加载标签
                dto.setTags(projectTagService.getTagsByProjectId(project.getId()));
                return dto;
            })
            .collect(Collectors.toList());
        
        // 如果指定了标签筛选，进一步过滤
        if (cmd.getTag() != null && !cmd.getTag().trim().isEmpty()) {
            String tagFilter = cmd.getTag().trim();
            projectDTOs = projectDTOs.stream()
                .filter(dto -> dto.getTags() != null && dto.getTags().contains(tagFilter))
                .collect(Collectors.toList());
        }
        
        return projectDTOs;
    }

    @Transactional(readOnly = true)
    public ProjectDTO get(Long projectId) {
        Project project = projectGateway.findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        // RPC call to Task Module using structured DTO
        List<TaskRemoteDTO> tasks = taskFeignClient.getTasksByProjectId(projectId);

        ProjectDTO projectDTO = ProjectMapper.INSTANCE.toDTO(project);
        projectDTO.setTasks(tasks);
        
        // 加载标签
        projectDTO.setTags(projectTagService.getTagsByProjectId(projectId));
        
        // 加载关联的团队
        List<Long> teamIds = projectTeamService.getTeamIdsByProjectId(projectId);
        if (!teamIds.isEmpty()) {
            List<TeamDTO> teams = new ArrayList<>();
            for (Long teamId : teamIds) {
                Result<TeamRemoteDTO> teamResult = userFeignClient.getTeam(teamId);
                if (teamResult != null && teamResult.getData() != null) {
                    TeamRemoteDTO remoteTeam = teamResult.getData();
                    TeamDTO teamDTO = TeamDTO.builder()
                        .id(remoteTeam.getId())
                        .name(remoteTeam.getName())
                        .description(remoteTeam.getDescription())
                        .build();
                    teams.add(teamDTO);
                }
            }
            projectDTO.setTeams(teams);
        }
        
        // 加载项目成员
        List<Long> memberIds = projectTeamService.getMemberIdsByProjectId(projectId);
        if (!memberIds.isEmpty()) {
            List<ProjectMemberDTO> members = new ArrayList<>();
            for (Long userId : memberIds) {
                Result<UserRemoteDTO> userResult = userFeignClient.getUser(userId);
                if (userResult != null && userResult.getData() != null) {
                    UserRemoteDTO remoteUser = userResult.getData();
                    // 获取成员在项目中的角色
                    String role = projectTeamService.getMemberRole(projectId, userId);
                    ProjectMemberDTO memberDTO = ProjectMemberDTO.builder()
                        .id(remoteUser.getId())
                        .userId(remoteUser.getId())
                        .name(remoteUser.getName())
                        .email(remoteUser.getEmail())
                        .avatar(remoteUser.getAvatar())
                        .role(role != null ? role : "member")
                        .status(remoteUser.getStatus())
                        .createdAt(remoteUser.getCreatedAt())
                        .build();
                    members.add(memberDTO);
                }
            }
            projectDTO.setProjectMembers(members);
        }
        
        return projectDTO;
    }

    @Transactional(readOnly = true)
    public ProjectStatsDTO getProjectStats(Long projectId) {
        List<TaskRemoteDTO> tasks = taskFeignClient.getTasksByProjectId(projectId);

        int active = (int) tasks.stream()
            .filter(t -> !"DONE".equals(t.getStatus()) && !"CANCELLED".equals(t.getStatus()))
            .count();
        int completed = (int) tasks.stream().filter(t -> "DONE".equals(t.getStatus())).count();
        int highPriority =
            (int) tasks.stream().filter(t -> "HIGH".equalsIgnoreCase(t.getPriority())).count();

        return ProjectStatsDTO.builder()
            .activeTasks(active)
            .completedTasks(completed)
            .highPriorityTasks(highPriority)
            .teamVelocity("94%") // Mocked for now, can be calculated later
            .build();
    }

    @Transactional
    public ProjectDTO update(UpdateProjectCmd cmd) {
        Project project = projectGateway.findById(cmd.getId());
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }
        project.update(cmd.getName(), cmd.getDescription(), cmd.getOwnerId(), cmd.getDeadline(),
            cmd.getStartDate(), cmd.getPriority(), cmd.getCoverImage());
        Project savedProject = projectGateway.save(project);
        
        // 更新项目标签
        if (cmd.getTags() != null) {
            projectTagService.saveProjectTags(cmd.getId(), cmd.getTags());
        }
        
        // 更新项目-团队关联
        // 如果teamIds不为null（包括空列表），都更新关联关系
        // 如果teamIds为null，则不更新关联关系（保持原有关联）
        if (cmd.getTeamIds() != null) {
            // 获取旧的团队ID列表
            List<Long> oldTeamIds = projectTeamService.getTeamIdsByProjectId(cmd.getId());
            List<Long> newTeamIds = cmd.getTeamIds();
            
            // 找出被移除的团队ID
            List<Long> removedTeamIds = oldTeamIds.stream()
                .filter(teamId -> !newTeamIds.contains(teamId))
                .collect(Collectors.toList());
            
            // 如果有关联的成员，删除被移除团队下的成员
            if (!removedTeamIds.isEmpty()) {
                removeMembersFromRemovedTeams(cmd.getId(), removedTeamIds);
            }
            
            // 更新团队关联
            projectTeamService.saveProjectTeams(cmd.getId(), cmd.getTeamIds());
            
            // 更新成员数量
            updateProjectMembersCount(cmd.getId());
        }
        
        return ProjectMapper.INSTANCE.toDTO(savedProject);
    }
    
    /**
     * 删除被移除团队下的项目成员
     */
    private void removeMembersFromRemovedTeams(Long projectId, List<Long> removedTeamIds) {
        // 获取项目当前所有成员
        List<Long> allMemberIds = projectTeamService.getMemberIdsByProjectId(projectId);
        
        // 获取被移除团队下的所有用户ID
        Set<Long> usersInRemovedTeams = new HashSet<>();
        for (Long teamId : removedTeamIds) {
            Result<List<UserRemoteDTO>> result = userFeignClient.listTeamMembers(teamId);
            if (result != null && result.getData() != null) {
                result.getData().forEach(user -> usersInRemovedTeams.add(user.getId()));
            }
        }
        
        // 删除属于被移除团队的项目成员
        for (Long userId : allMemberIds) {
            if (usersInRemovedTeams.contains(userId)) {
                projectTeamService.removeProjectMember(projectId, userId);
            }
        }
    }

    @Transactional
    public void delete(Long id) {
        // 删除关联的团队、成员和标签
        projectTeamService.deleteProjectTeams(id);
        projectTeamService.deleteProjectMembers(id);
        projectTagService.deleteProjectTags(id);
        projectGateway.delete(id);
    }

    @Transactional
    public void addProjectMembers(AddProjectMembersCmd cmd) {
        projectTeamService.addProjectMembers(cmd.getProjectId(), cmd.getUserIds(), cmd.getRole());
        // 更新项目的成员数量
        updateProjectMembersCount(cmd.getProjectId());
    }

    @Transactional
    public void removeProjectMember(Long projectId, Long userId) {
        projectTeamService.removeProjectMember(projectId, userId);
        // 更新项目的成员数量
        updateProjectMembersCount(projectId);
    }

    /**
     * 更新项目的成员数量
     */
    private void updateProjectMembersCount(Long projectId) {
        Project project = projectGateway.findById(projectId);
        if (project != null) {
            // 查询当前项目的实际成员数量
            List<Long> memberIds = projectTeamService.getMemberIdsByProjectId(projectId);
            int memberCount = memberIds != null ? memberIds.size() : 0;
            project.updateMembersCount(memberCount);
            projectGateway.save(project);
        }
    }

    @Transactional
    public void updateProjectMemberRole(Long projectId, Long userId, String role) {
        projectTeamService.updateProjectMemberRole(projectId, userId, role);
    }

    @Transactional(readOnly = true)
    public List<TeamDTO> getAvailableTeams() {
        Result<List<TeamRemoteDTO>> result = userFeignClient.listTeams();
        if (result != null && result.getData() != null) {
            return result.getData().stream()
                .map(remoteTeam -> TeamDTO.builder()
                    .id(remoteTeam.getId())
                    .name(remoteTeam.getName())
                    .description(remoteTeam.getDescription())
                    .build())
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<ProjectMemberDTO> getTeamMembers(Long teamId) {
        Result<List<UserRemoteDTO>> result = userFeignClient.listTeamMembers(teamId);
        if (result != null && result.getData() != null) {
            return result.getData().stream()
                .map(remoteUser -> ProjectMemberDTO.builder()
                    .id(remoteUser.getId())
                    .userId(remoteUser.getId())
                    .name(remoteUser.getName())
                    .email(remoteUser.getEmail())
                    .avatar(remoteUser.getAvatar())
                    .role("member")
                    .status(remoteUser.getStatus())
                    .createdAt(remoteUser.getCreatedAt())
                    .build())
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<ProjectMemberDTO> searchUsers(String keyword) {
        Result<List<UserRemoteDTO>> result = userFeignClient.searchUsers(keyword);
        if (result != null && result.getData() != null) {
            return result.getData().stream()
                .map(remoteUser -> ProjectMemberDTO.builder()
                    .id(remoteUser.getId())
                    .userId(remoteUser.getId())
                    .name(remoteUser.getName())
                    .email(remoteUser.getEmail())
                    .avatar(remoteUser.getAvatar())
                    .role("member")
                    .status(remoteUser.getStatus())
                    .createdAt(remoteUser.getCreatedAt())
                    .build())
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 根据团队ID列表查询所有用户（去重）
     * 用于项目添加成员时，展示项目关联团队下的所有用户
     */
    @Transactional(readOnly = true)
    public List<ProjectMemberDTO> getUsersByTeamIds(List<Long> teamIds) {
        if (teamIds == null || teamIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 使用Set去重，key为userId
        java.util.Map<Long, ProjectMemberDTO> userMap = new java.util.HashMap<>();
        
        // 遍历每个团队ID，查询该团队下的用户
        for (Long teamId : teamIds) {
            Result<List<UserRemoteDTO>> result = userFeignClient.listTeamMembers(teamId);
            if (result != null && result.getData() != null) {
                result.getData().forEach(remoteUser -> {
                    // 如果用户已存在，跳过（去重）
                    if (!userMap.containsKey(remoteUser.getId())) {
                        ProjectMemberDTO memberDTO = ProjectMemberDTO.builder()
                            .id(remoteUser.getId())
                            .userId(remoteUser.getId())
                            .name(remoteUser.getName())
                            .email(remoteUser.getEmail())
                            .avatar(remoteUser.getAvatar())
                            .role("member")
                            .status(remoteUser.getStatus())
                            .createdAt(remoteUser.getCreatedAt())
                            .build();
                        userMap.put(remoteUser.getId(), memberDTO);
                    }
                });
            }
        }
        
        return new ArrayList<>(userMap.values());
    }

    /**
     * 检查移除团队对项目成员的影响
     * 返回会被删除的成员列表
     */
    @Transactional(readOnly = true)
    public List<ProjectMemberDTO> checkTeamRemovalImpact(Long projectId, List<Long> newTeamIds) {
        // 获取当前关联的团队ID
        List<Long> currentTeamIds = projectTeamService.getTeamIdsByProjectId(projectId);
        
        // 找出被移除的团队ID
        List<Long> removedTeamIds = currentTeamIds.stream()
            .filter(teamId -> newTeamIds == null || !newTeamIds.contains(teamId))
            .collect(Collectors.toList());
        
        if (removedTeamIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取项目当前所有成员
        List<Long> allMemberIds = projectTeamService.getMemberIdsByProjectId(projectId);
        if (allMemberIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取被移除团队下的所有用户ID
        Set<Long> usersInRemovedTeams = new HashSet<>();
        for (Long teamId : removedTeamIds) {
            Result<List<UserRemoteDTO>> result = userFeignClient.listTeamMembers(teamId);
            if (result != null && result.getData() != null) {
                result.getData().forEach(user -> usersInRemovedTeams.add(user.getId()));
            }
        }
        
        // 找出会被删除的成员（属于被移除团队的项目成员）
        List<ProjectMemberDTO> affectedMembers = new ArrayList<>();
        for (Long userId : allMemberIds) {
            if (usersInRemovedTeams.contains(userId)) {
                Result<UserRemoteDTO> userResult = userFeignClient.getUser(userId);
                if (userResult != null && userResult.getData() != null) {
                    UserRemoteDTO remoteUser = userResult.getData();
                    String role = projectTeamService.getMemberRole(projectId, userId);
                    ProjectMemberDTO memberDTO = ProjectMemberDTO.builder()
                        .id(remoteUser.getId())
                        .userId(remoteUser.getId())
                        .name(remoteUser.getName())
                        .email(remoteUser.getEmail())
                        .avatar(remoteUser.getAvatar())
                        .role(role != null ? role : "member")
                        .status(remoteUser.getStatus())
                        .createdAt(remoteUser.getCreatedAt())
                        .build();
                    affectedMembers.add(memberDTO);
                }
            }
        }
        
        return affectedMembers;
    }

    /**
     * 切换项目收藏状态
     */
    @Transactional
    public ProjectDTO toggleStar(Long projectId) {
        Project project = projectGateway.findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }
        project.toggleStar();
        Project savedProject = projectGateway.save(project);
        ProjectDTO dto = ProjectMapper.INSTANCE.toDTO(savedProject);
        dto.setTags(projectTagService.getTagsByProjectId(projectId));
        return dto;
    }

    /**
     * 归档项目
     */
    @Transactional
    public ProjectDTO archive(Long projectId) {
        Project project = projectGateway.findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }
        project.archive();
        Project savedProject = projectGateway.save(project);
        ProjectDTO dto = ProjectMapper.INSTANCE.toDTO(savedProject);
        dto.setTags(projectTagService.getTagsByProjectId(projectId));
        return dto;
    }

    /**
     * 取消归档项目
     */
    @Transactional
    public ProjectDTO unarchive(Long projectId) {
        Project project = projectGateway.findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }
        project.unarchive();
        Project savedProject = projectGateway.save(project);
        ProjectDTO dto = ProjectMapper.INSTANCE.toDTO(savedProject);
        dto.setTags(projectTagService.getTagsByProjectId(projectId));
        return dto;
    }

    /**
     * 复制/克隆项目
     */
    @Transactional
    public ProjectDTO cloneProject(CloneProjectCmd cmd) {
        Project sourceProject = projectGateway.findById(cmd.getSourceProjectId());
        if (sourceProject == null) {
            throw new IllegalArgumentException("Source project not found");
        }
        
        // 创建新项目
        Project newProject = Project.create(
            cmd.getNewName() != null ? cmd.getNewName() : sourceProject.getName() + " (Copy)",
            sourceProject.getDescription(),
            sourceProject.getOwnerId(),
            sourceProject.getDeadline(),
            sourceProject.getStartDate(),
            sourceProject.getPriority(),
            sourceProject.getCoverImage()
        );
        Long newProjectId = projectGateway.save(newProject).getId();
        
        // 复制团队关联
        if (cmd.getCloneTeams() != null && cmd.getCloneTeams()) {
            List<Long> teamIds = projectTeamService.getTeamIdsByProjectId(cmd.getSourceProjectId());
            if (!teamIds.isEmpty()) {
                projectTeamService.saveProjectTeams(newProjectId, teamIds);
            }
        }
        
        // 复制成员
        if (cmd.getCloneMembers() != null && cmd.getCloneMembers()) {
            List<Long> memberIds = projectTeamService.getMemberIdsByProjectId(cmd.getSourceProjectId());
            if (!memberIds.isEmpty()) {
                // 复制成员时保持原有角色
                for (Long userId : memberIds) {
                    String role = projectTeamService.getMemberRole(cmd.getSourceProjectId(), userId);
                    projectTeamService.addProjectMembers(newProjectId, List.of(userId), role);
                }
                updateProjectMembersCount(newProjectId);
            }
        }
        
        // 复制标签
        if (cmd.getCloneTags() != null && cmd.getCloneTags()) {
            List<String> tags = projectTagService.getTagsByProjectId(cmd.getSourceProjectId());
            if (!tags.isEmpty()) {
                projectTagService.saveProjectTags(newProjectId, tags);
            }
        }
        
        // 返回新项目
        return get(newProjectId);
    }
}