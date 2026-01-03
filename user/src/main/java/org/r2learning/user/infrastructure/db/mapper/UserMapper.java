package org.r2learning.user.infrastructure.db.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.r2learning.user.domain.team.Team;
import org.r2learning.user.domain.user.User;
import org.r2learning.user.infrastructure.db.dataobject.TeamDO;
import org.r2learning.user.infrastructure.db.dataobject.UserDO;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    TeamDO teamToDO(Team team);

    Team teamToEntity(TeamDO teamDO);

    UserDO userToDO(User user);

    User userToEntity(UserDO userDO);
}

