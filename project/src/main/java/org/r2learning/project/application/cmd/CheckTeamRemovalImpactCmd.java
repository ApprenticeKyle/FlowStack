package org.r2learning.project.application.cmd;

import java.util.List;
import lombok.Data;

/**
 * 检查移除团队对项目成员影响的命令
 */
@Data
public class CheckTeamRemovalImpactCmd {
    private Long projectId;
    private List<Long> newTeamIds; // 新的团队ID列表
}

