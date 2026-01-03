package org.r2learning.user.infrastructure.db.repository;

import java.util.List;
import org.r2learning.user.infrastructure.db.dataobject.TeamDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTeamRepository extends JpaRepository<TeamDO, Long> {
    List<TeamDO> findByOwnerId(Long ownerId);
}

