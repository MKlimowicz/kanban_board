package com.example.kanban.services.project;

import com.example.kanban.dao.person.PersonDao;
import com.example.kanban.dao.project.ProjectDao;
import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonDto;
import com.example.kanban.dto.PersonForProjectDto;
import com.example.kanban.dto.ProjectDto;
import com.example.kanban.exception.person.NotFoundPersonException;
import com.example.kanban.exception.person.PersonIsAlreadyAddedToProjectException;
import com.example.kanban.exception.project.ProjectNotExistsException;
import com.example.kanban.exception.project.ProjectWithThisNameExistsException;
import com.example.kanban.mapper.ProjectMapper;
import com.example.kanban.model.Note;
import com.example.kanban.model.Person;
import com.example.kanban.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

class ProjectServiceImplTest {


    @InjectMocks
    private ProjectServiceImpl projectServiceImpl;


    @Mock
    private PersonDao personDao;

    @Mock
    private ProjectDao projectDao;


    private ProjectMapper projectMapper;
    private ProjectDto projectDtoBeforeUpdate;
    private Project projectBeforeUpdate;
    private Project projectAfterUpdate;
    private ProjectDto projectDto;
    private Project projectWithId;
    private Project projectWithOutId;
    private Person person;
    private Person person1;
    private PersonForProjectDto personForProjectDto;
    private ArrayList persons;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        projectMapper = new ProjectMapper();
        projectDtoBeforeUpdate = projectMapper.toDto(getListProject().get(0));
        projectBeforeUpdate = getListProject().get(0);
        projectAfterUpdate = getListProject().get(0);
        projectAfterUpdate.setName("new");
        projectWithId = getListProject().get(0);
        projectWithOutId = getListProject().get(0);
        projectWithOutId.setId(null);
        projectDto = projectMapper.toDto(projectWithId);


        person = new Person();
        person.setId(1);
        person.setName("Test");
        person.setLastName("LongTest");

        person1 = new Person();
        person1.setName("Test");
        person1.setId(1);
        person1.setName("Test");

        persons = new ArrayList<>();
        persons.add(person);
        projectWithId.setPersons(persons);

        personForProjectDto = new PersonForProjectDto();
        personForProjectDto.setPersonId(1);
        personForProjectDto.setProjectId(1);

    }


    @Test
    public void shouldReturnListNote() {
        //given
        given(projectDao.findAll()).willReturn(getListProject());
        //when
        List<ProjectDto> projects = projectServiceImpl.getProjects();
        //then
        assertThat(projects, hasSize(3));
        assertThat(projects.get(0), instanceOf(ProjectDto.class));
        assertThat(projects.get(2).getName(), equalTo("Projekt3"));
    }

    @Test
    public void shouldThrowExceptionIfProjectNotExists() {
        //given
        given(projectDao.findById(1)).willReturn(Optional.empty());
        ProjectDto projectDto = projectMapper.toDto(getListProject().get(0));
        //when
        //then
        assertThrows(ProjectNotExistsException.class, () -> {
            projectServiceImpl.updateProject(projectDto);
        });
    }

    @Test
    public void shouldReturnUpdatedProject() {
        //given
        given(projectDao.findById(1)).willReturn(Optional.ofNullable(getListProject().get(0)));
        given(projectDao.update(projectBeforeUpdate)).willReturn(projectAfterUpdate);
        //when
        ProjectDto projectDtoUpdated = projectServiceImpl.updateProject(projectDtoBeforeUpdate);
        //then
        assertThat(projectDtoUpdated, instanceOf(ProjectDto.class));
        assertThat(projectDtoUpdated.getName(), equalTo("new"));
    }

    @Test
    public void shouldThrowIfProjectExists() {
        //given
        given(projectDao.findByName("Projekt1"))
                .willReturn(Optional.ofNullable(projectWithId));
        //when
        //then
        assertThrows(ProjectWithThisNameExistsException.class, () -> {
            projectServiceImpl.saveProject(projectDto);
        });
    }

    @Test
    public void shouldSaveAndReturnMapProject() {
        //given
        given(projectDao.findByName("Projekt1")).willReturn(Optional.empty());
        given(projectDao.save(projectWithOutId)).willReturn(projectWithId);
        //when
        ProjectDto projectDto = projectServiceImpl.saveProject(projectMapper.toDto(projectWithOutId));
        //then
        assertThat(projectDto.getId(), is(1));
        assertThat(projectDto.getName(), equalTo("Projekt1"));
    }


    @Test
    public void shouldReturnDeletedProject() {
        //given
        given(projectDao.findById(1)).willReturn(Optional.ofNullable(projectWithId));
        //when
        ProjectDto projectDto = projectServiceImpl.deleteProject(1);
        //then
        assertThat(projectDto.getId(), is(1));
        assertThat(projectDto.getName(), equalTo("Projekt1"));
    }

    @Test
    public void shouldReturnProjectWithGivenId() {
        //given
        given(projectDao.findById(1)).willReturn(Optional.ofNullable(projectWithId));
        //when
        ProjectDto projectDto = projectServiceImpl.getProjectById(1);
        //then
        assertThat(projectDto.getId(), is(1));
        assertThat(projectDto.getName(), equalTo("Projekt1"));
    }


    //--------------------- addPersonToProject(PersonForProjectDto personForProjectDto) ---------------------

    @Test
    public void shouldThrowExceptionIfPersonNotExists() {
        //given
        given(personDao.findById(1)).willReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundPersonException.class, () -> {
            projectServiceImpl.addPersonToProject(personForProjectDto);
        });
    }

    @Test
    public void shouldThrowExceptionIfPersonIsAlreadyAdded() {
        //given
        given(personDao.findById(1)).willReturn(Optional.ofNullable(person));
        given(projectDao.findById(1)).willReturn(Optional.ofNullable(projectWithId));
        //when
        //then
        assertThrows(PersonIsAlreadyAddedToProjectException.class, () -> {
            projectServiceImpl.addPersonToProject(personForProjectDto);
        });
    }

    @Test
    public void shouldAddPersonToProject() {
        //given
        given(personDao.findById(1)).willReturn(Optional.ofNullable(person1));
        given(projectDao.findById(1)).willReturn(Optional.ofNullable(projectWithId));

        given(projectDao.update(projectWithId)).willReturn(projectWithId);
        //when
        PersonDto person = projectServiceImpl.addPersonToProject(personForProjectDto);
        //then
        assertThat(person.getId(), is(1));
        assertThat(person.getName(), equalTo(person1.getName()));
    }



    //--------------------- getPersonFromProject(Integer projectId) ---------------------

    @Test
    public void shouldReturnPersonFromProject() {
        //given
        given(projectDao.findById(1)).willReturn(Optional.ofNullable(projectWithId));
        //when
        List<PersonDto> personsFromProject = projectServiceImpl.getPersonFromProject(1);
        //then
        assertThat(personsFromProject, hasSize(1));
        assertThat(personsFromProject.get(0).getId(), is(person.getId()));
    }

    //---------------------  getNoteFromProject(Integer projectId) ---------------------

    @Test
    public void shouldReturnNoteFromProject() {
        //given
        projectWithId.setNotes(getListNote());
        given(projectDao.findById(1)).willReturn(Optional.ofNullable(projectWithId));
        //when
        List<NoteDto> noteDtoFromProject = projectServiceImpl.getNoteFromProject(1);
        //then
        assertThat(noteDtoFromProject, hasSize(3));
        assertThat(noteDtoFromProject.get(0).getId(), is(1));
        assertThat(noteDtoFromProject.get(1).getTitle(), equalTo("Zadanie2"));
    }


    private List<Note> getListNote() {
        Note note = new Note();
        note.setId(1);
        note.setTitle("Zadanie1");
        Note note1 = new Note();
        note1.setId(2);
        note1.setTitle("Zadanie2");
        Note note2 = new Note();
        note2.setId(3);
        note2.setTitle("Zadanie3");
        return Arrays.asList(note, note1, note2);
    }

    private List<Project> getListProject() {
        Project project1 = new Project();
        project1.setId(1);
        project1.setName("Projekt1");
        Project project2 = new Project();
        project2.setId(2);
        project2.setName("Projekt2");
        Project project3 = new Project();
        project3.setId(3);
        project3.setName("Projekt3");
        return Arrays.asList(project1, project2, project3);
    }
}