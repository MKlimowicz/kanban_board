package com.example.kanban.mapper;

import com.example.kanban.dto.ProjectDto;
import com.example.kanban.model.Project;

public class ProjectMapper implements Mapper<Project,ProjectDto> {
    @Override
    public Project toEntity(ProjectDto dto) {
        Project entity = new Project();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }

    @Override
    public ProjectDto toDto(Project entity) {
        ProjectDto dto = new ProjectDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
