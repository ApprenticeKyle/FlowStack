package org.r2learning.user.infrastructure.db.dataobject;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

