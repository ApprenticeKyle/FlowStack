package org.r2learning.project.client.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 远程团队DTO（从user-service获取）
 * 用于Feign客户端调用，不对外暴露
 */
@Data
@Builder
public class TeamRemoteDTO {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}

