package org.r2learning.user.interfaces.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.r2learning.common.domain.common.Result;
import org.r2learning.user.application.cmd.AddUsersToTeamCmd;
import org.r2learning.user.application.cmd.CreateTeamCmd;
import org.r2learning.user.application.cmd.CreateUserCmd;
import org.r2learning.user.application.cmd.UpdateTeamCmd;
import org.r2learning.user.application.cmd.UpdateUserCmd;
import org.r2learning.user.application.service.UserApplicationService;
import org.r2learning.user.interfaces.web.dto.TeamDTO;
import org.r2learning.user.interfaces.web.dto.UserDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserApplicationService userApplicationService;

    // Team APIs
    @PostMapping("/teams")
    public Result<Long> createTeam(@RequestBody CreateTeamCmd cmd) {
        Long teamId = userApplicationService.createTeam(cmd);
        return Result.success(teamId);
    }

    @GetMapping("/teams")
    public Result<List<TeamDTO>> listTeams() {
        return Result.success(userApplicationService.listTeams());
    }

    @GetMapping("/teams/{id}")
    public Result<TeamDTO> getTeam(@PathVariable Long id) {
        return Result.success(userApplicationService.getTeam(id));
    }

    @PutMapping("/teams/{id}")
    public Result<TeamDTO> updateTeam(@PathVariable Long id, @RequestBody UpdateTeamCmd cmd) {
        cmd.setId(id);
        return Result.success(userApplicationService.updateTeam(cmd));
    }

    @DeleteMapping("/teams/{id}")
    public Result<Void> deleteTeam(@PathVariable Long id) {
        userApplicationService.deleteTeam(id);
        return Result.success();
    }

    // User APIs
    @PostMapping
    public Result<Long> createUser(@RequestBody CreateUserCmd cmd) {
        Long userId = userApplicationService.createUser(cmd);
        return Result.success(userId);
    }

    @GetMapping
    public Result<List<UserDTO>> listUsers() {
        return Result.success(userApplicationService.listUsers());
    }

    @GetMapping("/search")
    public Result<List<UserDTO>> searchUsers(@RequestParam String keyword) {
        return Result.success(userApplicationService.searchUsers(keyword));
    }

    @GetMapping("/{id}")
    public Result<UserDTO> getUser(@PathVariable Long id) {
        return Result.success(userApplicationService.getUser(id));
    }

    @PutMapping("/{id}")
    public Result<UserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserCmd cmd) {
        cmd.setId(id);
        return Result.success(userApplicationService.updateUser(cmd));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userApplicationService.deleteUser(id);
        return Result.success();
    }

    // Team-User relationship APIs
    @GetMapping("/teams/{teamId}/members")
    public Result<List<UserDTO>> listTeamMembers(@PathVariable Long teamId) {
        return Result.success(userApplicationService.listUsersByTeam(teamId));
    }

    @PostMapping("/teams/{teamId}/members")
    public Result<Void> addUsersToTeam(@PathVariable Long teamId, @RequestBody AddUsersToTeamCmd cmd) {
        cmd.setTeamId(teamId);
        userApplicationService.addUsersToTeam(cmd);
        return Result.success();
    }

    @DeleteMapping("/teams/{teamId}/members/{userId}")
    public Result<Void> removeUserFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {
        userApplicationService.removeUserFromTeam(userId);
        return Result.success();
    }
}

