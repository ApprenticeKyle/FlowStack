package org.r2learning.user.domain.user;

import java.time.LocalDateTime;
import lombok.Getter;

/**
 * User Aggregate Root
 */
@Getter
public class User {
    private Long id;
    private String name;
    private String email;
    private String avatar;
    private Long teamId;
    private String role; // owner, admin, member
    private String status; // online, away, offline
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(Long id, String name, String email, String avatar, Long teamId,
                String role, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.teamId = teamId;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String name, String email, String avatar, Long teamId, String role) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be empty");
        }
        return new User(null, name, email, avatar, teamId, role, "offline",
            LocalDateTime.now(), LocalDateTime.now());
    }

    public void update(String name, String email, String avatar) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (email != null && !email.trim().isEmpty()) {
            this.email = email;
        }
        if (avatar != null) {
            this.avatar = avatar;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void changeRole(String role) {
        if (role != null && (role.equals("owner") || role.equals("admin") || role.equals("member"))) {
            this.role = role;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void changeStatus(String status) {
        if (status != null && (status.equals("online") || status.equals("away") || status.equals("offline"))) {
            this.status = status;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void changeTeam(Long teamId) {
        this.teamId = teamId;
        this.updatedAt = LocalDateTime.now();
    }
}

