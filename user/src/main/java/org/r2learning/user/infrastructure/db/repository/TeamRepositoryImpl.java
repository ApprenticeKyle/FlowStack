package org.r2learning.user.infrastructure.db.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.r2learning.user.domain.team.Team;
import org.r2learning.user.domain.team.gateway.TeamGateway;
import org.r2learning.user.infrastructure.db.dataobject.TeamDO;
import org.r2learning.user.infrastructure.db.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamGateway {
    private final JpaTeamRepository jpaTeamRepository;

    @Override
    public Team save(Team team) {
        TeamDO teamDO = UserMapper.INSTANCE.teamToDO(team);
        TeamDO savedDO = jpaTeamRepository.save(teamDO);
        return UserMapper.INSTANCE.teamToEntity(savedDO);
    }

    @Override
    public Team findById(Long id) {
        return jpaTeamRepository.findById(id)
            .map(UserMapper.INSTANCE::teamToEntity)
            .orElse(null);
    }

    @Override
    public void delete(Long id) {
        jpaTeamRepository.deleteById(id);
    }

    @Override
    public List<Team> findAll() {
        return jpaTeamRepository.findAll().stream()
            .map(UserMapper.INSTANCE::teamToEntity)
            .toList();
    }

    @Override
    public List<Team> findByOwnerId(Long ownerId) {
        return jpaTeamRepository.findByOwnerId(ownerId).stream()
            .map(UserMapper.INSTANCE::teamToEntity)
            .toList();
    }
}

