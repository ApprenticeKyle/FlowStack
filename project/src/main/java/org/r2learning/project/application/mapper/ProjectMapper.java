package org.r2learning.project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.r2learning.project.domain.project.Project;
import org.r2learning.project.interfaces.web.dto.ProjectDTO;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper {
    
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectDTO toDTO(Project project);
    
    Project toDomain(ProjectDTO dto);
}