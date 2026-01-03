package org.r2learning.project.infrastructure.db.repository;

import java.util.List;
import org.r2learning.project.infrastructure.db.dataobject.ProjectMemberDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProjectMemberRepository extends JpaRepository<ProjectMemberDO, Long> {
    List<ProjectMemberDO> findByProjectId(Long projectId);
    
    void deleteByProjectId(Long projectId);
    
    void deleteByProjectIdAndUserId(Long projectId, Long userId);
    
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
}

