package org.r2learning.user.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.r2learning.user.domain.team.Team;
import org.r2learning.user.domain.user.User;
import org.r2learning.user.interfaces.web.dto.TeamDTO;
import org.r2learning.user.interfaces.web.dto.UserDTO;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Team mappings
    TeamDTO teamToDTO(Team team);

    // User mappings
    UserDTO userToDTO(User user);
}

