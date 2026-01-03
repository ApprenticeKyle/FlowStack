package org.r2learning.project.client.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 远程任务DTO（从task-service获取）
 * 用于Feign客户端调用，不对外暴露
 */
@Data
public class TaskRemoteDTO {
    private Long id;
    private String title;
    private String description;
    private String status; // String for simplicity in remote
    private String priority;
    private Long assigneeId;
    private Long reporterId;
    private Long projectId;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

