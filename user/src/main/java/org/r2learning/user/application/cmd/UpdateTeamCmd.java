package org.r2learning.user.application.cmd;

import lombok.Data;

@Data
public class UpdateTeamCmd {
    private Long id;
    private String name;
    private String description;
}

