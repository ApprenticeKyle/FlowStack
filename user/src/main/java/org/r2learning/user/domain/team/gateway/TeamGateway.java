package org.r2learning.user.domain.team.gateway;

import java.util.List;
import org.r2learning.user.domain.team.Team;

public interface TeamGateway {
    Team save(Team team);

    Team findById(Long id);

    void delete(Long id);

    List<Team> findAll();

    List<Team> findByOwnerId(Long ownerId);
}

