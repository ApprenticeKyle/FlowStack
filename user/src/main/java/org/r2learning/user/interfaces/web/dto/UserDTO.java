package org.r2learning.user.interfaces.web.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String avatar;
    private Long teamId;
    private String role; // owner, admin, member
    private String status; // online, away, offline
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

