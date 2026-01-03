package org.r2learning.project.application.cmd;

import java.util.List;
import lombok.Data;

/**
 * 根据团队ID列表查询用户的命令
 */
@Data
public class GetUsersByTeamIdsCmd {
    private List<Long> teamIds;
}

