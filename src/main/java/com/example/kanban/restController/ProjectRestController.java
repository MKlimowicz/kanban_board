package com.example.kanban.restController;


import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonDto;
import com.example.kanban.dto.PersonForProjectDto;
import com.example.kanban.dto.ProjectDto;
import com.example.kanban.services.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectRestController {

    private ProjectService projectService;

    @Autowired
    public ProjectRestController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @GetMapping()
    public List<ProjectDto> getProjects() {
        return projectService.getProjects();
    }


    @PostMapping()
    public ResponseEntity<ProjectDto> saveProject(@RequestBody ProjectDto dto) {
        checkID(dto.getId(),  "Project to save, can't have set id");
        ProjectDto savedProject = projectService.saveProject(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProject.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedProject);
    }


    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Integer projectId) {
        ProjectDto projectById = projectService.getProjectById(projectId);
        return ResponseEntity.ok(projectById);
    }


    @DeleteMapping("/{projectId}")
    public ResponseEntity<ProjectDto> deleteProjectById(@PathVariable Integer projectId) {
        checkID(projectId, "Id must by set when you try delete project");
        ProjectDto deletedProject = projectService.deleteProject(projectId);
        return ResponseEntity.ok(deletedProject);
    }

    @PutMapping()
    public ResponseEntity<ProjectDto> updateProject(@RequestBody ProjectDto dto) {
        checkID(dto.getId(), "Project to save, must have set id");
        ProjectDto updatedProject = projectService.updateProject(dto);
        return ResponseEntity.ok(updatedProject);
    }


    @PostMapping("/addPerson")
    public void addPersonForProject(@RequestBody PersonForProjectDto personForProjectDto) {
        Integer personId = personForProjectDto.getPersonId();
        Integer projectId = personForProjectDto.getProjectId();

        if (personId == null && projectId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You must provide the id of the person and project to which you want to add it"
            );
        }
        projectService.addPersonToProject(personForProjectDto);

    }

    @GetMapping("/persons/{projectId}")
    public List<PersonDto> getPersonsWithProjectById(@PathVariable Integer projectId) {
        checkID(projectId, "You must provide the id of the project ");
        return projectService.getPersonFromProject(projectId);
    }


    @GetMapping("/notes/{projectId}")
    public List<NoteDto> getNotesFromProject(@PathVariable Integer projectId) {
        checkID(projectId, "You must provide the id of the project ");
        return projectService.getNoteFromProject(projectId);
    }


    private void checkID(Integer projectId, String message) {
        if (projectId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }
}
