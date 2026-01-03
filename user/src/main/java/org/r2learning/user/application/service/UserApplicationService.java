package org.r2learning.user.application.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.r2learning.common.utils.UserContextHolder;
import org.r2learning.user.application.cmd.AddUsersToTeamCmd;
import org.r2learning.user.application.cmd.CreateTeamCmd;
import org.r2learning.user.application.cmd.CreateUserCmd;
import org.r2learning.user.application.cmd.UpdateTeamCmd;
import org.r2learning.user.application.cmd.UpdateUserCmd;
import org.r2learning.user.application.mapper.UserMapper;
import org.r2learning.user.domain.team.Team;
import org.r2learning.user.domain.team.gateway.TeamGateway;
import org.r2learning.user.domain.user.User;
import org.r2learning.user.domain.user.gateway.UserGateway;
import org.r2learning.user.interfaces.web.dto.TeamDTO;
import org.r2learning.user.interfaces.web.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final TeamGateway teamGateway;
    private final UserGateway userGateway;

    @Transactional
    public Long createTeam(CreateTeamCmd cmd) {
        Long ownerId = UserContextHolder.getCurrentUserId();
        cmd.setOwnerId(ownerId);
        Team team = Team.create(cmd.getName(), cmd.getDescription(), cmd.getOwnerId());
        return teamGateway.save(team).getId();
    }

    @Transactional(readOnly = true)
    public List<TeamDTO> listTeams() {
        List<Team> teams = teamGateway.findAll();
        return teams.stream()
            .map(UserMapper.INSTANCE::teamToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeamDTO getTeam(Long teamId) {
        Team team = teamGateway.findById(teamId);
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        TeamDTO teamDTO = UserMapper.INSTANCE.teamToDTO(team);
        // 加载团队成员
        List<User> members = userGateway.findByTeamId(teamId);
        teamDTO.setMembers(members.stream()
            .map(UserMapper.INSTANCE::userToDTO)
            .collect(Collectors.toList()));
        return teamDTO;
    }

    @Transactional
    public TeamDTO updateTeam(UpdateTeamCmd cmd) {
        Team team = teamGateway.findById(cmd.getId());
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        team.update(cmd.getName(), cmd.getDescription());
        return UserMapper.INSTANCE.teamToDTO(teamGateway.save(team));
    }

    @Transactional
    public void deleteTeam(Long teamId) {
        // 检查是否有成员
        List<User> members = userGateway.findByTeamId(teamId);
        if (!members.isEmpty()) {
            throw new IllegalStateException("Cannot delete team with members. Please remove members first.");
        }
        teamGateway.delete(teamId);
    }

    @Transactional
    public Long createUser(CreateUserCmd cmd) {
        // 验证团队存在
        Team team = teamGateway.findById(cmd.getTeamId());
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        User user = User.create(cmd.getName(), cmd.getEmail(), cmd.getAvatar(),
            cmd.getTeamId(), cmd.getRole() != null ? cmd.getRole() : "member");
        return userGateway.save(user).getId();
    }

    @Transactional(readOnly = true)
    public List<UserDTO> listUsers() {
        List<User> users = userGateway.findAll();
        return users.stream()
            .map(UserMapper.INSTANCE::userToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> listUsersByTeam(Long teamId) {
        List<User> users = userGateway.findByTeamId(teamId);
        return users.stream()
            .map(UserMapper.INSTANCE::userToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getUser(Long userId) {
        User user = userGateway.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return UserMapper.INSTANCE.userToDTO(user);
    }

    @Transactional
    public UserDTO updateUser(UpdateUserCmd cmd) {
        User user = userGateway.findById(cmd.getId());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        user.update(cmd.getName(), cmd.getEmail(), cmd.getAvatar());
        if (cmd.getRole() != null) {
            user.changeRole(cmd.getRole());
        }
        return UserMapper.INSTANCE.userToDTO(userGateway.save(user));
    }

    @Transactional
    public void deleteUser(Long userId) {
        userGateway.delete(userId);
    }

    @Transactional
    public void addUsersToTeam(AddUsersToTeamCmd cmd) {
        Team team = teamGateway.findById(cmd.getTeamId());
        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }
        List<User> users = userGateway.findByIds(cmd.getUserIds());
        for (User user : users) {
            user.changeTeam(cmd.getTeamId());
            if (cmd.getRole() != null) {
                user.changeRole(cmd.getRole());
            }
            userGateway.save(user);
        }
    }

    @Transactional
    public void removeUserFromTeam(Long userId) {
        User user = userGateway.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        user.changeTeam(null);
        userGateway.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> searchUsers(String keyword) {
        // 简单实现，实际应该使用更复杂的搜索逻辑
        List<User> allUsers = userGateway.findAll();
        return allUsers.stream()
            .filter(user -> user.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                user.getEmail().toLowerCase().contains(keyword.toLowerCase()))
            .map(UserMapper.INSTANCE::userToDTO)
            .collect(Collectors.toList());
    }
}

