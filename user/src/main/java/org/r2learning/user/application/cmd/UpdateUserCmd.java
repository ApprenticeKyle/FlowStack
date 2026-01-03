package org.r2learning.user.application.cmd;

import lombok.Data;

@Data
public class UpdateUserCmd {
    private Long id;
    private String name;
    private String email;
    private String avatar;
    private String role;
}

