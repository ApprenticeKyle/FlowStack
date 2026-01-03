package org.r2learning.user.infrastructure.db.dataobject;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "teams")
public class TeamDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Long ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

