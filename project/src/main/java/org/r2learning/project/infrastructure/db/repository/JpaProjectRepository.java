package org.r2learning.project.infrastructure.db.repository;

import java.util.List;
import org.r2learning.project.infrastructure.db.dataobject.ProjectDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProjectRepository extends JpaRepository<ProjectDO, Long>, JpaSpecificationExecutor<ProjectDO> {
    
    @Query("SELECT p FROM ProjectDO p WHERE " +
           "(:name IS NULL OR p.name LIKE %:name%) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:ownerId IS NULL OR p.ownerId = :ownerId) AND " +
           "(:archived IS NULL OR p.archived = :archived) AND " +
           "(:starred IS NULL OR p.starred = :starred) AND " +
           "(:priority IS NULL OR p.priority = :priority)")
    List<ProjectDO> findByCriteria(@Param("name") String name, 
                                   @Param("status") String status, 
                                   @Param("ownerId") Long ownerId,
                                   @Param("archived") Boolean archived,
                                   @Param("starred") Boolean starred,
                                   @Param("priority") String priority);
}