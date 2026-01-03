package org.r2learning.project.infrastructure.db.dataobject;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "projects")
public class ProjectDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
