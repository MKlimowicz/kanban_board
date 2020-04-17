package com.example.kanban.services.project;

import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonDto;
import com.example.kanban.dto.PersonForProjectDto;
import com.example.kanban.dto.ProjectDto;
import com.example.kanban.model.Project;

import java.util.List;

public interface ProjectService {
    List<ProjectDto> getProjects();
    ProjectDto updateProject(ProjectDto projectDto);
    ProjectDto saveProject(ProjectDto projectDto);
    ProjectDto deleteProject(Integer projectId);
    ProjectDto getProjectById(Integer projectId);
    void addPersonToProject(PersonForProjectDto personForProjectDto);
    List<PersonDto> getPersonFromProject(Integer projectId);
}