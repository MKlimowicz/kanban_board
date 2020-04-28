package com.example.kanban.services.project;

import com.example.kanban.dao.person.PersonDao;
import com.example.kanban.dao.project.ProjectDao;
import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonDto;
import com.example.kanban.dto.PersonForProjectDto;
import com.example.kanban.dto.ProjectDto;
import com.example.kanban.exception.person.PersonIsAlreadyAddedToProjectException;
import com.example.kanban.exception.project.ProjectNotExistsException;
import com.example.kanban.exception.person.NotFoundPersonException;
import com.example.kanban.exception.project.ProjectWithThisNameExistsException;
import com.example.kanban.mapper.NoteMapper;
import com.example.kanban.mapper.PersonMapper;
import com.example.kanban.mapper.ProjectMapper;
import com.example.kanban.model.Person;
import com.example.kanban.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProjectServiceImpl implements ProjectService {

    private PersonDao personDao;
    private ProjectMapper projectMapper;
    private PersonMapper personMapper;
    private ProjectDao projectDao;

    @Autowired
    public ProjectServiceImpl(PersonDao personDao, ProjectDao projectDao) {
        this.personDao = personDao;
        this.projectMapper = new ProjectMapper();
        this.personMapper = new PersonMapper();
        this.projectDao = projectDao;
    }

    @Override
    public List<ProjectDto> getProjects() {
        return projectDao
                .findAll()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDto updateProject(ProjectDto projectDto) {
        getProject(projectDto.getId());
        return mapAndUpdate(projectDto);
    }

    @Override
    public ProjectDto saveProject(ProjectDto projectDto) {
        Optional<Project> projectByName = projectDao.findByName(projectDto.getName());
        projectByName.ifPresent((e) -> {
            throw new ProjectWithThisNameExistsException("project name: " + e.getName());
        });

        return mapAndSave(projectDto);
    }

    @Override
    public ProjectDto deleteProject(Integer projectId) {
        Project project = getProject(projectId);
        projectDao.remove(project);
        return projectMapper.toDto(project);
    }

    @Override
    public ProjectDto getProjectById(Integer projectId) {
        return projectMapper.toDto(getProject(projectId));
    }


    @Override
    public PersonDto addPersonToProject(PersonForProjectDto personForProjectDto) {
        Person person = getPerson(personForProjectDto.getPersonId());
        Project project = getProject(personForProjectDto.getProjectId());

        List<Person> personsForProject = project.getPersons();

        if (personsForProject.contains(person)) {
            throw new PersonIsAlreadyAddedToProjectException();
        }
        personsForProject.add(person);
        project.setPersons(personsForProject);
        return personMapper.toDto(person);
    }

    @Override
    public List<PersonDto> getPersonFromProject(Integer projectId) {
        return getProject(projectId)
                .getPersons()
                .stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoteDto> getNoteFromProject(Integer projectId) {
        return getProject(projectId)
                .getNotes()
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }


    private ProjectDto mapAndSave(ProjectDto projectDto) {
        Project entity = projectMapper.toEntity(projectDto);
        Project savedProject = projectDao.save(entity);
        return projectMapper.toDto(savedProject);
    }

    private ProjectDto mapAndUpdate(ProjectDto projectDto) {
        Project entity = projectMapper.toEntity(projectDto);
        Project savedProject = projectDao.update(entity);
        return projectMapper.toDto(savedProject);
    }


    private Project getProject(Integer projectId) {
        return projectDao
                .findById(projectId)
                .orElseThrow(ProjectNotExistsException::new);
    }

    private Person getPerson(Integer personId) {
        Optional<Person> personById = personDao.findById(personId);
        personById.orElseThrow(() -> {
            throw new NotFoundPersonException("Not found person by id: " + personId);
        });
        return personById.get();
    }

}
