package org.r2learning.project.application.cmd;

import lombok.Data;

@Data
public class UpdateProjectCmd {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
}
