package org.r2learning.user.domain.team;

import java.time.LocalDateTime;
import lombok.Getter;

/**
 * Team Aggregate Root
 */
@Getter
public class Team {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Team(Long id, String name, String description, Long ownerId,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Team create(String name, String description, Long ownerId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be empty");
        }
        return new Team(null, name, description, ownerId,
            LocalDateTime.now(), LocalDateTime.now());
    }

    public void update(String name, String description) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
        this.updatedAt = LocalDateTime.now();
    }
}

