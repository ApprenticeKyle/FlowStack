package org.r2learning.project.interfaces.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.r2learning.project.client.dto.TaskRemoteDTO;

@Data
@Builder
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private List<TaskRemoteDTO> tasks; // 从task-service获取的远程任务数据
    private String status;
    private LocalDate deadline;
    private LocalDate startDate;
    private Integer members;
    private Integer progress;
    private String priority;
    private Boolean starred;
    private Boolean archived;
    private String coverImage;
    private List<String> tags; // 项目标签列表
    private LocalDateTime createdAt;
    private List<TeamDTO> teams; // 关联的团队（从user-service获取后转换）
    private List<ProjectMemberDTO> projectMembers; // 项目成员（从user-service获取后转换）
}
