package org.r2learning.project.client;

import java.util.List;
import org.r2learning.common.domain.common.Result;
import org.r2learning.project.client.dto.TeamRemoteDTO;
import org.r2learning.project.client.dto.UserRemoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * User Service Feign客户端
 * 用于调用user-service的接口，返回的是RemoteDTO
 */
@FeignClient(name = "user-service", path = "/api/users")
public interface UserFeignClient {
    
    @GetMapping("/teams")
    Result<List<TeamRemoteDTO>> listTeams();
    
    @GetMapping("/teams/{id}")
    Result<TeamRemoteDTO> getTeam(@PathVariable Long id);

    @GetMapping("/search")
    Result<List<UserRemoteDTO>> searchUsers(@RequestParam String keyword);

    @GetMapping("/{id}")
    Result<UserRemoteDTO> getUser(@PathVariable Long id);

    @GetMapping("/teams/{teamId}/members")
    Result<List<UserRemoteDTO>> listTeamMembers(@PathVariable Long teamId);
}

