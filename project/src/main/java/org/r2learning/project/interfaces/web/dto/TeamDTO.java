package org.r2learning.project.interfaces.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamDTO {
    private Long id;
    private String name;
    private String description;
}

