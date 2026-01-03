package org.r2learning.project.interfaces.web.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectMemberDTO {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String avatar;
    private String role; // owner, admin, member
    private String status; // online, away, offline
    private LocalDateTime createdAt;
}

