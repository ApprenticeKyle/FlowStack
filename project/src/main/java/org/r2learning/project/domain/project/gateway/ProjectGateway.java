package org.r2learning.project.domain.project.gateway;

import java.util.List;
import org.r2learning.project.application.cmd.ListProjectCmd;
import org.r2learning.project.domain.project.Project;

public interface ProjectGateway {
    Project save(Project project);

    Project findById(Long id);

    void delete(Long id);

    List<Project> findByCriteria(ListProjectCmd cmd);
}