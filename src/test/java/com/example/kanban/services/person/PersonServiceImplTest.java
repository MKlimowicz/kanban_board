package com.example.kanban.services.person;

import com.example.kanban.dao.person.PersonDao;
import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonDto;
import com.example.kanban.dto.ProjectDto;
import com.example.kanban.exception.person.NotFoundPersonException;
import com.example.kanban.mapper.PersonMapper;
import com.example.kanban.mapper.ProjectMapper;
import com.example.kanban.model.Note;
import com.example.kanban.model.Person;
import com.example.kanban.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

class PersonServiceImplTest {



    @InjectMocks
    private PersonServiceImpl personServiceImpl;

    @Mock
    private PersonDao personDao;


    private PersonMapper personMapper;
    private ProjectMapper projectMapper;
    private Person person;
    private Person person1;
    private Person personWithOutId;
    private PersonDto personWithOutIdDto;
    private PersonDto personDto;
    private PersonDto personDto1;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        personMapper = new PersonMapper();
        projectMapper = new ProjectMapper();
        person = getPersonList().get(0);
        person1 = getPersonList().get(0);
        personDto = personMapper.toDto(person);
        personDto1 = personMapper.toDto(person1);
        person.setNotes(getListNote());
        person.setProjects(getListProject());
        personWithOutId = getPersonList().get(0);
        personWithOutId.setId(null);
        personWithOutIdDto = personMapper.toDto(personWithOutId);
    }


    // ---------------- List<PersonDto> getListPerson() ------------

    @Test
    public void shouldReturnPersonDtoList() {
        //given
        given(personDao.findAll()).willReturn(getPersonList());
        //when
        List<PersonDto> listPerson = personServiceImpl.getListPerson();
        //then
        assertThat(listPerson, hasSize(3));
        assertThat(listPerson.get(0).getId(), is(1));
        assertThat(listPerson.get(1).getName(),equalTo("Test"));

    }

    // ------------ PersonDto updatePerson(PersonDto personDto) -----------
    @Test
    public void shouldReturnUpdatedPerson() {
        //given
        given(personDao.findById(1)).willReturn(Optional.ofNullable(person));
        given(personDao.update(person1)).willReturn(person1);
        //when
        PersonDto personDto = personServiceImpl.updatePerson(this.personDto1);
        //then
        assertThat(personDto.getId(), is(1));
        assertThat(person1.getName(), equalTo(person1.getName()));
    }

    // ----------------------  public PersonDto getPersonById(Integer personId)  ----------------

    @Test
    public void shouldThrowExceptionIfPersonByIdNotExists() {
        //given
        given(personDao.findById(1)).willReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundPersonException.class, () -> {
            personServiceImpl.getPersonById(1);
        });
    }

    @Test
    public void shouldReturnPersonDtoFoundById() {
        //given
        given(personDao.findById(1)).willReturn(Optional.ofNullable(person));
        //when
        PersonDto personById = personServiceImpl.getPersonById(1);
        //then
        assertThat(personById.getId() , is(1));
        assertThat(personById.getName(), equalTo(personById.getName()));
    }

    // -------------  PersonDto savePerson(PersonDto personDto)  ------

    @Test
    public void shouldReturnSavedAndMapPerson(){
        //given
        given(personDao.save(personWithOutId)).willReturn(person);
        //when
        PersonDto personDto = personServiceImpl.savePerson(personWithOutIdDto);
        //then
        assertThat(personDto.getId(), is(1));
        assertThat(personDto.getName(), equalTo(personWithOutId.getName()));
    }


    // ------------------ PersonDto deletePerson(Integer personId) ---------------
    @Test
    public void shouldReturnDeletedPerson() {
        //given
        given(personDao.findById(1)).willReturn(Optional.ofNullable(person));
        //when
        PersonDto personDto = personServiceImpl.deletePerson(1);
        //then
        assertThat(personDto.getId() , is(1));
        assertThat(personDto.getName(), equalTo(person.getName()));
    }


    // ---------- List<NoteDto> getListNoteFromPerson(Integer personId) -----------

    @Test
    public void shouldReturnNoteDtoAddedToPerson(){
        //given
        given(personDao.findById(1)).willReturn(Optional.ofNullable(person));
        //when
        List<NoteDto> listNoteFromPerson = personServiceImpl.getListNoteFromPerson(1);
        //then
        assertThat(listNoteFromPerson , hasSize(3));
    }


    // -------------- List<ProjectDto> getPersonProjectList(Integer personId) -------------
    @Test
    public void shouldReturnProjectListForPerson() {
        //given
        given(personDao.findById(1)).willReturn(Optional.of(person));
        //when
        List<ProjectDto> personProjectList = personServiceImpl.getPersonProjectList(1);
        //then
        assertThat(personProjectList, hasSize(3));
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