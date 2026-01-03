package org.r2learning.project.infrastructure.db.repository;

import org.r2learning.project.infrastructure.db.dataobject.ProjectTagDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaProjectTagRepository extends JpaRepository<ProjectTagDO, Long> {
    List<ProjectTagDO> findByProjectId(Long projectId);
    void deleteByProjectId(Long projectId);
    void deleteByProjectIdAndTag(Long projectId, String tag);
}

