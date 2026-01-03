package org.r2learning.user.application.cmd;

import lombok.Data;

@Data
public class CreateTeamCmd {
    private String name;
    private String description;
    private Long ownerId;
}

