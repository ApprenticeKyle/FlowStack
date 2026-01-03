package org.r2learning.project.infrastructure.db.dataobject;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "project_teams", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"project_id", "team_id"})
})
public class ProjectTeamDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}

