package org.r2learning.project.infrastructure.db.dataobject;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "project_tags", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"project_id", "tag"})
})
public class ProjectTagDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "tag", nullable = false, length = 50)
    private String tag;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}

