package org.r2learning.project.application.cmd;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class UpdateProjectCmd {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private LocalDate deadline;
    private LocalDate startDate;
    private String priority;
    private String coverImage;
    private List<Long> teamIds; // 关联的团队ID列表
    private List<String> tags; // 项目标签列表
}
