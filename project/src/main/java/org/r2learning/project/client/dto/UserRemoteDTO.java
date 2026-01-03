package org.r2learning.project.client.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 远程用户DTO（从user-service获取）
 * 用于Feign客户端调用，不对外暴露
 */
@Data
@Builder
public class UserRemoteDTO {
    private Long id;
    private String name;
    private String email;
    private String avatar;
    private Long teamId;
    private String role;
    private String status;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}

