package org.r2learning.project.application.cmd;

import java.util.List;
import lombok.Data;

@Data
public class CloneProjectCmd {
    private Long sourceProjectId;
    private String newName;
    private Boolean cloneTeams = true; // 是否复制团队关联
    private Boolean cloneMembers = false; // 是否复制成员（默认不复制）
    private Boolean cloneTags = true; // 是否复制标签
}

