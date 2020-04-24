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
import com.example.kanban.services.person.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@WebMvcTest(PersonRestController.class)
public class PersonRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PersonService personService;


    private String api;
    private PersonMapper personMapper;
    private ProjectMapper projectMapper;
    private Person person;
    private PersonDto personDto;

    @Before
    public void setUp() {
        api = "/api/person";
        personMapper = new PersonMapper();
        projectMapper = new ProjectMapper();

        person = getPersonList().get(0);
        personDto = personMapper.toDto(person);
    }


    // -------------- List<PersonDto> getListPerson()  -----------------

    @Test
    public void shouldReturnPersonList() throws Exception {
        //given
        given(personService.getListPerson()).willReturn(getPersonDtoList());
        //when
        //then
        mockMvc.perform(get(api))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }


    // ------------- ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto personDto) -----------------

    @Test
    public void shouldThrowExceptionIfPersonIdIsNull() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(post(api,nullValue()))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldReturnNewPerson() throws Exception {
        //given
        given(personService.savePerson(personDto)).willReturn(personDto);
        //when
        //then
        mockMvc.perform(post(api)
                .contentType(APPLICATION_JSON)
                .content(asJsonString(personDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", equalTo("Test")));

    }


    // ------------- ResponseEntity<PersonDto> updatePerson(@RequestBody PersonDto personDto) ----------

    @Test
    public void shouldReturnUpdatedPerson() throws Exception {
        //given
        given(personService.updatePerson(personDto)).willReturn(personDto);
        //when
        //then
        mockMvc.perform(put(api)
                .contentType(APPLICATION_JSON)
                .content(asJsonString(personDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", equalTo("Test")));

    }

    // ----------- ResponseEntity<PersonDto> deletePerson(@PathVariable Integer personId) ------------

    @Test
    public void shouldReturnDeletedPersonById() throws Exception {
        //given
        given(personService.deletePerson(1)).willReturn(personDto);
        //when
        //then
        mockMvc.perform(delete(api + "/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", equalTo("Test")));
    }


    // ------------ List<NoteDto> getNotesForPerson(@PathVariable Integer personId) ------

    @Test
    public  void shouldReturnNotesFromPerson() throws Exception {
        given(personService.getListNoteFromPerson(1)).willReturn(getNoteDtoList());
        //when
        //then
        mockMvc.perform(get(api + "/notes/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    // -------------- ResponseEntity<PersonDto> getPersonById(@PathVariable Integer personId) ---------

    @Test
    public void shouldReturnPersonById() throws Exception {
        //given
        given(personService.getPersonById(1)).willReturn(personDto);
        //when
        //then
        mockMvc.perform(get(api + "/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", equalTo("Test")));
    }

    // ---------------- List<ProjectDto> getPersonProjectListById(@PathVariable Integer personId) --------------------

    @Test
    public void shouldReturnPersonProjectListById() throws Exception {
        given(personService.getPersonProjectList(1)).willReturn(getProjectDto());
        //when
        //then
        mockMvc.perform(get(api + "/projects/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }




    // ------------ List<NoteDto> getNoteFromPersonForProject(@RequestBody PersonForProjectDto personForProjectDto)-----

    @Test
    public  void shouldReturnNoteFromPersonForProject() throws Exception {
        PersonForProjectDto personForProjectDto = new PersonForProjectDto();
        personForProjectDto.setProjectId(1);
        personForProjectDto.setPersonId(1);

        given(personService.getListNoteFromPersonForProject(personForProjectDto)).willReturn(getNoteDtoList());
        //when
        //then
        mockMvc.perform(get(api + "/projects/notes", personForProjectDto)
                .contentType(APPLICATION_JSON)
                .content(asJsonString(personForProjectDto)))
                  .andExpect(status().isOk())
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
        project2.setId(2);
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