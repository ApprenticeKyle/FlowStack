package org.r2learning.project.infrastructure.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.r2learning.project.infrastructure.db.dataobject.ProjectMemberDO;
import org.r2learning.project.infrastructure.db.dataobject.ProjectTeamDO;
import org.r2learning.project.infrastructure.db.repository.JpaProjectMemberRepository;
import org.r2learning.project.infrastructure.db.repository.JpaProjectTeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectTeamService {

    private final JpaProjectTeamRepository projectTeamRepository;
    private final JpaProjectMemberRepository projectMemberRepository;

    @Transactional
    public void saveProjectTeams(Long projectId, List<Long> teamIds) {
        // 先删除旧的关联
        projectTeamRepository.deleteByProjectId(projectId);
        
        // 如果teamIds不为null且不为空，创建新的关联
        if (teamIds != null && !teamIds.isEmpty()) {
            // 去重：过滤掉重复的团队ID
            List<Long> uniqueTeamIds = teamIds.stream()
                .distinct()
                .filter(teamId -> teamId != null) // 过滤null值
                .collect(Collectors.toList());
            
            if (!uniqueTeamIds.isEmpty()) {
                List<ProjectTeamDO> projectTeams = uniqueTeamIds.stream()
                    .map(teamId -> {
                        ProjectTeamDO projectTeam = new ProjectTeamDO();
                        projectTeam.setProjectId(projectId);
                        projectTeam.setTeamId(teamId);
                        projectTeam.setCreatedAt(LocalDateTime.now());
                        return projectTeam;
                    })
                    .collect(Collectors.toList());
                
                // 批量保存前，再次检查并过滤已存在的记录（防止并发问题）
                List<ProjectTeamDO> newProjectTeams = projectTeams.stream()
                    .filter(pt -> !projectTeamRepository.findByProjectIdAndTeamId(
                        pt.getProjectId(), 
                        pt.getTeamId()
                    ).isPresent())
                    .collect(Collectors.toList());
                
                if (!newProjectTeams.isEmpty()) {
                    projectTeamRepository.saveAll(newProjectTeams);
                }
            }
        }
        // 如果teamIds为null或空列表，只删除旧关联，不创建新关联（相当于清空所有关联）
    }

    @Transactional(readOnly = true)
    public List<Long> getTeamIdsByProjectId(Long projectId) {
        return projectTeamRepository.findByProjectId(projectId).stream()
            .map(ProjectTeamDO::getTeamId)
            .collect(Collectors.toList());
    }

    @Transactional
    public void addProjectMembers(Long projectId, List<Long> userIds, String role) {
        if (userIds == null || userIds.isEmpty()) {
            return;
        }
        String finalRole = role != null ? role : "member";
        List<ProjectMemberDO> members = userIds.stream()
            .filter(userId -> !projectMemberRepository.existsByProjectIdAndUserId(projectId, userId))
            .map(userId -> {
                ProjectMemberDO member = new ProjectMemberDO();
                member.setProjectId(projectId);
                member.setUserId(userId);
                member.setRole(finalRole);
                member.setCreatedAt(LocalDateTime.now());
                member.setUpdatedAt(LocalDateTime.now());
                return member;
            })
            .collect(Collectors.toList());
        projectMemberRepository.saveAll(members);
    }

    @Transactional
    public void removeProjectMember(Long projectId, Long userId) {
        projectMemberRepository.deleteByProjectIdAndUserId(projectId, userId);
    }

    @Transactional
    public void updateProjectMemberRole(Long projectId, Long userId, String role) {
        projectMemberRepository.findByProjectId(projectId).stream()
            .filter(member -> member.getUserId().equals(userId))
            .findFirst()
            .ifPresent(member -> {
                member.setRole(role);
                member.setUpdatedAt(LocalDateTime.now());
                projectMemberRepository.save(member);
            });
    }

    @Transactional(readOnly = true)
    public List<Long> getMemberIdsByProjectId(Long projectId) {
        return projectMemberRepository.findByProjectId(projectId).stream()
            .map(ProjectMemberDO::getUserId)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public String getMemberRole(Long projectId, Long userId) {
        return projectMemberRepository.findByProjectId(projectId).stream()
            .filter(member -> member.getUserId().equals(userId))
            .findFirst()
            .map(ProjectMemberDO::getRole)
            .orElse("member");
    }

    @Transactional
    public void deleteProjectTeams(Long projectId) {
        projectTeamRepository.deleteByProjectId(projectId);
    }

    @Transactional
    public void deleteProjectMembers(Long projectId) {
        projectMemberRepository.deleteByProjectId(projectId);
    }
}

