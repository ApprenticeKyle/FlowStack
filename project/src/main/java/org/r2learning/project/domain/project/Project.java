package org.r2learning.project.domain.project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

/**
 * Project Aggregate Root
 */
@Getter
public class Project {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private LocalDate deadline;
    private LocalDate startDate;
    private Integer members;
    private Integer progress;
    private String priority;
    private Boolean starred;
    private Boolean archived;
    private String coverImage;

    public Project(Long id, String name, String description, Long ownerId, String status,
                   LocalDate deadline, LocalDate startDate,
                   Integer members, Integer progress, String priority,
                   Boolean starred, Boolean archived, String coverImage,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.status = status;
        this.deadline = deadline;
        this.startDate = startDate;
        this.members = members;
        this.progress = progress;
        this.priority = priority;
        this.starred = starred;
        this.archived = archived;
        this.coverImage = coverImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Project create(String name, String description, Long ownerId,
                                 LocalDate deadline, LocalDate startDate, String priority,
                                 String coverImage) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be empty");
        }
        return new Project(null, name, description, ownerId, "planning", deadline, startDate,
            0, 0, priority != null ? priority : "MEDIUM", false, false, coverImage,
            LocalDateTime.now(), LocalDateTime.now());
    }

    public void update(String name, String description, Long ownerId, LocalDate deadline,
                       LocalDate startDate, String priority, String coverImage) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
        if (ownerId != null) {
            this.ownerId = ownerId;
        }
        if (deadline != null) {
            this.deadline = deadline;
        }
        if (startDate != null) {
            this.startDate = startDate;
        }
        if (priority != null) {
            this.priority = priority;
        }
        if (coverImage != null) {
            this.coverImage = coverImage;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void toggleStar() {
        this.starred = !this.starred;
        this.updatedAt = LocalDateTime.now();
    }

    public void archive() {
        this.archived = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void unarchive() {
        this.archived = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 更新成员数量
     */
    public void updateMembersCount(Integer count) {
        if (count != null && count >= 0) {
            this.members = count;
            this.updatedAt = LocalDateTime.now();
        }
    }
}
