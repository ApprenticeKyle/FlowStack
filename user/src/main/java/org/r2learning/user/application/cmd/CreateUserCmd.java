package org.r2learning.user.application.cmd;

import lombok.Data;

@Data
public class CreateUserCmd {
    private String name;
    private String email;
    private String avatar;
    private Long teamId;
    private String role; // owner, admin, member
}

