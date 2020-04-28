package com.example.kanban.restController;

import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonDto;
import com.example.kanban.dto.PersonForNoteDto;
import com.example.kanban.dto.ProjectDto;
import com.example.kanban.mapper.NoteMapper;
import com.example.kanban.mapper.PersonMapper;
import com.example.kanban.mapper.ProjectMapper;
import com.example.kanban.model.Note;
import com.example.kanban.model.Person;
import com.example.kanban.model.Project;
import com.example.kanban.services.category.CategoryService;
import com.example.kanban.services.note.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@WebMvcTest(NoteRestController.class)
public class NoteRestControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    private String api;
    private PersonMapper personMapper;
    private ProjectMapper projectMapper;
    private NoteDto noteDto;
    private Note note;
    private Note noteWithOutId;
    private NoteDto noteDtoWithOutId;

    @Before
    public void setUp() {
        api = "/api/note";
        personMapper = new PersonMapper();
        note = getListNote().get(0);
        noteDto = getNoteDtoList().get(0);

        noteWithOutId = getListNote().get(1);
        noteDtoWithOutId = getNoteDtoList().get(1);
    }


    // -------------  List<NoteDto> getListNote() -----------

    @Test
    public void shouldReturnListNoteDto() throws Exception {
        //given
        given(noteService.getListNote()).willReturn(getNoteDtoList());
        // when
        // then
        mockMvc.perform(get(api)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }


    // ---------------------- ResponseEntity<NoteDto> saveNote(@RequestBody NoteDto dto)  ------------

    @Test
    public void shouldThrowExceptionIfNoteDtoHaveSetId() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(post(api, noteDto))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnSavedNote() throws Exception {
        //given
        given(noteService.saveNote(noteDtoWithOutId)).willReturn(noteDto);
        //when
        //then
        mockMvc.perform(post(api)
                .contentType(APPLICATION_JSON)
                .content(asJsonString(noteDtoWithOutId)))
                     .andExpect(status().isCreated())
                     .andExpect(jsonPath("$.id", is(1)));
    }

    // ------------ List<NoteDto> getListNoteByCategoryId(@PathVariable Integer categoryId) -----
    @Test
    public void shouldReturnListNoteByCategoryId() throws Exception {
        //given
        given(noteService.getListNoteByCategoryId(1)).willReturn(getNoteDtoList());
        //when
        //then
        mockMvc.perform(get(api + "/category/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    // ---------- ResponseEntity<NoteDto> deleteNoteById(@PathVariable Integer noteId)-----------

    @Test
    public void shouldReturnDeletedNote() throws Exception {
        //given
        given(noteService.deleteNote(1)).willReturn(noteDto);
        //when
        //then
        mockMvc.perform(delete(api + "/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", equalTo("Zadanie1")));
    }

    // --------- public ResponseEntity<NoteDto> updateNote(@RequestBody NoteDto dto) -----------------

    @Test
    public void shouldReturnUpdatedNote() throws Exception {
        //given
        given(noteService.updateNote(noteDto)).willReturn(noteDto);
        //when
        //then
        mockMvc.perform(put(api, noteDto)
                .contentType(APPLICATION_JSON)
                .content(asJsonString(noteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", equalTo("Zadanie1")));
    }


    // ----------------  public ResponseEntity<NoteDto> addOwnerPerson(@RequestBody PersonForNoteDto personForNoteDto)------------

    @Test
    public void shouldThrowExceptionIfNoteIdIsNull() throws Exception {
        //given
        PersonForNoteDto personForNoteDto = new PersonForNoteDto();
        personForNoteDto.setPersonId(1);
        given(noteService.addOwnerPerson(personForNoteDto)).willReturn(noteDto);
        //when
        //then
        mockMvc.perform(put(api + "/addPerson")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldThrowExceptionIfPersonIdIsNull() throws Exception {
        //given
        PersonForNoteDto personForNoteDto = new PersonForNoteDto();
        personForNoteDto.setNoteId(1);
        given(noteService.addOwnerPerson(personForNoteDto)).willReturn(noteDto);
        //when
        //then
        mockMvc.perform(put(api + "/addPerson")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNoteWithAddedPerson() throws Exception {
        //given
        PersonForNoteDto personForNoteDto = new PersonForNoteDto();
        personForNoteDto.setNoteId(1);
        given(noteService.addOwnerPerson(personForNoteDto)).willReturn(noteDto);
        //when
        //then
        mockMvc.perform(put(api + "/addPerson")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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