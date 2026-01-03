package org.r2learning.project.infrastructure.db.repository;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.r2learning.project.application.cmd.ListProjectCmd;
import org.r2learning.project.domain.project.Project;
import org.r2learning.project.domain.project.gateway.ProjectGateway;
import org.r2learning.project.infrastructure.db.dataobject.ProjectDO;
import org.r2learning.project.infrastructure.db.mapper.ProjectMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectGateway {
    private final JpaProjectRepository jpaProjectRepository;

    @Override
    public Project save(Project project) {
        ProjectDO projectDO = ProjectMapper.INSTANCE.toDO(project);
        ProjectDO savedDO = jpaProjectRepository.save(projectDO);
        return ProjectMapper.INSTANCE.toEntity(savedDO);
    }

    @Override
    public Project findById(Long id) {
        return jpaProjectRepository.findById(id).map(ProjectMapper.INSTANCE::toEntity).orElse(null);
    }

    @Override
    public void delete(Long id) {
        jpaProjectRepository.deleteById(id);
    }

    @Override
    public List<Project> findByCriteria(ListProjectCmd cmd) {
        List<ProjectDO> projectDOs = jpaProjectRepository.findByCriteria(
            cmd.getName(),
            cmd.getStatus(),
            cmd.getOwnerId(),
            cmd.getArchived() != null ? cmd.getArchived() : false, // 默认不显示归档项目
            cmd.getStarred(),
            cmd.getPriority()
        );

        // 应用排序
        List<Project> projects = projectDOs.stream()
            .map(ProjectMapper.INSTANCE::toEntity)
            .toList();

        // 简单的内存排序（如果数据量大，应该移到数据库查询中）
        if (cmd.getSortBy() != null && !cmd.getSortBy().isEmpty()) {
            Comparator<Project> comparator = switch (cmd.getSortBy().toLowerCase()) {
                case "name" -> Comparator.comparing(Project::getName);
                case "createdat" -> Comparator.comparing(Project::getCreatedAt);
                case "updatedat" -> Comparator.comparing(Project::getUpdatedAt);
                case "progress" -> Comparator.comparing(Project::getProgress);
                default -> Comparator.comparing(Project::getCreatedAt);
            };

            if ("desc".equalsIgnoreCase(cmd.getSortOrder())) {
                comparator = comparator.reversed();
            }

            return projects.stream().sorted(comparator).toList();
        }

        return projects;
    }
}