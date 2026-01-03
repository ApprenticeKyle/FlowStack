package org.r2learning.user.infrastructure.db.repository;

import java.util.List;
import org.r2learning.user.infrastructure.db.dataobject.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<UserDO, Long> {
    List<UserDO> findByTeamId(Long teamId);

    UserDO findByEmail(String email);

    List<UserDO> findByIdIn(List<Long> ids);
}

