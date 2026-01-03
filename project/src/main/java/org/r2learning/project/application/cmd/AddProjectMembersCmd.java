package org.r2learning.project.application.cmd;

import java.util.List;
import lombok.Data;

@Data
public class AddProjectMembersCmd {
    private Long projectId;
    private List<Long> userIds;
    private String role; // 默认角色
}

