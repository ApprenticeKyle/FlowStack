package org.r2learning.user.application.cmd;

import java.util.List;
import lombok.Data;

@Data
public class AddUsersToTeamCmd {
    private Long teamId;
    private List<Long> userIds;
    private String role; // 默认角色
}

