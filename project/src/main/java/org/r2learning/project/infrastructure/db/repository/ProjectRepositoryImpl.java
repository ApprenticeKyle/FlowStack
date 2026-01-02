package org.r2learning.project.infrastructure.db.repository;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
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
    public Collection<Project> findAll() {
        return jpaProjectRepository.findAll().stream()
            .map(ProjectMapper.INSTANCE::toEntity)
            .toList();
    }
}
