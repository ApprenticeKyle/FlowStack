package org.r2learning.project.infrastructure.db.repository;

import java.util.List;
import org.r2learning.project.infrastructure.db.dataobject.ProjectTeamDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProjectTeamRepository extends JpaRepository<ProjectTeamDO, Long> {
    List<ProjectTeamDO> findByProjectId(Long projectId);
    
    void deleteByProjectId(Long projectId);
    
    void deleteByProjectIdAndTeamId(Long projectId, Long teamId);
    
    java.util.Optional<ProjectTeamDO> findByProjectIdAndTeamId(Long projectId, Long teamId);
}

