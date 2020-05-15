package com.example.kanban.restController;

import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonDto;
import com.example.kanban.dto.PersonForProjectDto;
import com.example.kanban.dto.ProjectDto;
import com.example.kanban.mapper.NoteMapper;
import com.example.kanban.mapper.PersonMapper;
import com.example.kanban.mapper.ProjectMapper;
import com.example.kanban.model.Note;
import com.example.kanban.model.Person;
import com.example.kanban.model.Project;
import com.example.kanban.services.project.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@WebMvcTest(ProjectRestController.class)
public class ProjectRestControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;


    private String api;
    private PersonMapper personMapper = new PersonMapper();
    private ProjectMapper projectMapper = new ProjectMapper();
    private Project project;
    private ProjectDto projectDto;
    private Project projectWithOutId;
    private ProjectDto projectDtoWithOutId;

    @Before
    public void setUp() throws Exception {
        api = "/api/project";
        project = getListProject().get(0);
        projectDto = getProjectDto().get(0);
        projectWithOutId = getListProject().get(1);
        projectDtoWithOutId = getProjectDto().get(1);
    }


    @Test
    public void shouldReturnListProject() throws Exception {
        //given
        given(projectService.getProjects()).willReturn(getProjectDto());
        //when
        //then
        mockMvc.perform(get(api)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    // --------------- ResponseEntity<ProjectDto> saveProject(@RequestBody ProjectDto dto) ------------

    @Test
    public void shouldThrowExceptionIfProjectToSaveHaveSetID() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(post(api, projectDto))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnSavedProject() throws Exception {
        //given
        given(projectService.saveProject(projectDtoWithOutId)).willReturn(projectDto);
        //when
        //then
        mockMvc.perform(post(api)
                .content(asJsonString(projectDtoWithOutId))
                .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
    }

    // ----- ResponseEntity<ProjectDto> getProjectById(@PathVariable Integer projectId)  -----

    @Test
    public void shouldReturnProjectById() throws Exception {
        //given
        given(projectService.getProjectById(1)).willReturn(projectDto);
        //when
        //then
        mockMvc.perform(get(api + "/{projectId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    // ---  ResponseEntity<ProjectDto> deleteProjectById(@PathVariable Integer projectId) ----

    @Test
    public void shouldReturnDeletedProjectById() throws Exception {
        //given
        given(projectService.deleteProject(1)).willReturn(projectDto);
        //when
        //then
        mockMvc.perform(delete(api + "/{projectId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    // ---  ResponseEntity<ProjectDto> updateProject(@RequestBody ProjectDto dto) ----

    @Test
    public void shouldReturnUpdatedProject() throws Exception {
        //given
        given(projectService.updateProject(projectDto)).willReturn(projectDto);
        //when
        //then
        mockMvc.perform(put(api)
                .content(asJsonString(projectDto))
                .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)));
    }


    // --- addPersonForProject(@RequestBody PersonForProjectDto personForProjectDto) ----

    @Test
    public void shouldThrowExceptionIfProjectIdIsNull() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(post(api + "/addPersons")
                .content(asJsonString(Collections.EMPTY_LIST))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void shouldAddPersonIdToProject() throws Exception {
        //given
        PersonForProjectDto personForProjectDto = new PersonForProjectDto();
        personForProjectDto.setPersonId(1);
        personForProjectDto.setProjectId(1);
        given(projectService
                .addPersonListToProject(Collections.singletonList(personForProjectDto)))
                .willReturn(Collections.singletonList(personForProjectDto));
        //when
        //then
        mockMvc.perform(post(api + "/addPersons")
                .content(asJsonString(Collections.singletonList(personForProjectDto)))
                .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk());
    }


    // -- List<PersonDto> getPersonsWithProjectById(@PathVariable Integer projectId)---
    @Test
    public void shouldReturnListPersonForProject() throws Exception{
        //given
        given(projectService.getPersonFromProject(1)).willReturn(getPersonDtoList());
        //when
        //then
        mockMvc.perform(get(api + "/persons/{projectId}", 1))
                .andExpect(jsonPath("$", hasSize(3)));
    }


    // -- List<NoteDto> getNotesFromProject(@PathVariable Integer projectId) ---
    @Test
    public void shouldReturnListNoteWithProject() throws Exception{
        //given
        given(projectService.getNoteFromProject(1)).willReturn(getNoteDtoList());
        //when
        //then
        mockMvc.perform(get(api + "/notes/{projectId}", 1))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    private List<PersonDto> getPersonDtoList() {
        return getPersonList()
                .stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }


    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    List<Person> getPersonList() {
        Person person = new Person();
        person.setId(1);
        person.setName("Test");
        person.setLastName("Test");
        Person person1 = new Person();
        person1.setId(2);
        person1.setName("Test");
        person1.setLastName("Test");
        Person person2 = new Person();
        person2.setId(3);
        person2.setName("Test");
        person2.setLastName("Test");

        return Arrays.asList(person, person1, person2);
    }

    private List<Note> getListNote() {
        Note note = new Note();
        note.setId(1);
        note.setTitle("Zadanie1");
        note.setContent("Opis1");
        Note note1 = new Note();
        note1.setId(2);
        note1.setTitle("Zadanie2");
        note1.setContent("Opis2");
        Note note2 = new Note();
        note2.setId(3);
        note2.setTitle("Zadanie3");
        note2.setContent("Opis3");
        return Arrays.asList(note, note1, note2);
    }


    private List<NoteDto> getNoteDtoList() {
        return getListNote()
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }


    private List<Project> getListProject() {
        Project project1 = new Project();
        project1.setId(1);
        project1.setName("Projekt1");
        Project project2 = new Project();
        project2.setName("Projekt2");
        Project project3 = new Project();
        project3.setId(3);
        project3.setName("Projekt3");
        return Arrays.asList(project1, project2, project3);
    }

    private List<ProjectDto> getProjectDto() {
        return getListProject()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }


}