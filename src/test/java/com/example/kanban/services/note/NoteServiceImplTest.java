package com.example.kanban.services.note;

import com.example.kanban.dao.category.CategoryDao;
import com.example.kanban.dao.note.NoteDao;
import com.example.kanban.dao.person.PersonDao;
import com.example.kanban.dao.project.ProjectDao;
import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonForNoteDto;
import com.example.kanban.exception.category.NotFoundCategoryException;
import com.example.kanban.exception.note.EmptyNoteException;
import com.example.kanban.exception.note.NoteNotSetProjectException;
import com.example.kanban.exception.person.NotFoundPersonException;
import com.example.kanban.exception.project.ProjectNotExistsException;
import com.example.kanban.mapper.NoteMapper;
import com.example.kanban.model.Category;
import com.example.kanban.model.Note;
import com.example.kanban.model.Person;
import com.example.kanban.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

class NoteServiceImplTest {

    @InjectMocks
    private NoteServiceImpl noteService;

    @Mock
    private CategoryDao categoryDao;
    @Mock
    private ProjectDao projectDao;
    @Mock
    private PersonDao personDao;
    @Mock
    private NoteDao noteDao;

    private Note note;
    private Note noteWithIdNull;
    private NoteDto noteDto;
    private NoteDto noteDtoWithIdNull;
    private Note noteWithOutPerson;
    private NoteDto noteDtoWithOutPerson;
    private Person person;
    private PersonForNoteDto personForNoteDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        note = getListNote().get(0);
        noteDto = NoteMapper.toDto(note);

        noteWithIdNull = getListNote().get(0);
        noteWithIdNull.setId(null);
        noteDtoWithIdNull = NoteMapper.toDto(note);

        noteWithOutPerson = getListNote().get(0);
        noteWithOutPerson.setId(null);
        noteWithOutPerson.setPerson(null);
        noteDtoWithOutPerson = NoteMapper.toDto(noteWithOutPerson);

        person = getPerson();
        person.setProjects(Arrays.asList(getProject()));

        personForNoteDto = new PersonForNoteDto();
        personForNoteDto.setNoteId(2);
        personForNoteDto.setPersonId(1);

    }

    // ------------ List<NoteDto> getListNote() {  ---------------------
    @Test
    public void shouldReturnNoteList() {
        //given
        given(noteDao.findAll()).willReturn(getListNote());
        //when
        List<NoteDto> listNote = noteService.getListNote();
        //then
        assertThat(listNote, hasSize(3));
        assertThat(listNote.get(0).getId(), is(1));
    }


    // ------------ NoteDto updateNote(NoteDto noteDto) ----------------------

    @Test
    public void shouldReturnUpdatedNote() {
        //given
        given(noteDao.findById(1)).willReturn(Optional.ofNullable(note));
        given(noteDao.update(note)).willReturn(note);
        //when
        NoteDto noteDto = noteService.updateNote(this.noteDto);
        //then
        assertThat(noteDto.getId(), is(1));
        assertThat(noteDto.getTitle(), equalTo(note.getTitle()));

    }

    // ----------- NoteDto saveNote(NoteDto dto) ---------------------


    @Test
    public void shouldThrowExceptionIfContentAndTitleIsNotSet() {
        //given
        //when
        //then
        assertThrows(EmptyNoteException.class, () -> {
            noteService.saveNote(new NoteDto());
        });
    }

    @Test
    public void shouldThrowExceptionIfCategoryNotExists() {
        //given
        given(categoryDao.findById(1)).willReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundCategoryException.class, () -> {
            noteService.saveNote(noteDto);
        });
    }

    @Test
    public void shouldThrowExceptionIfProjectNotExists() {
        //given
        given(categoryDao.findById(1)).willReturn(Optional.ofNullable(getCategory()));
        given(projectDao.findById(1)).willReturn(Optional.empty());
        //when
        //then
        assertThrows(ProjectNotExistsException.class, () -> {
            noteService.saveNote(noteDto);
        });
    }

    @Test
    public void shouldThrowExceptionIfPersonNotExists() {
        //given
        given(categoryDao.findById(1)).willReturn(Optional.ofNullable(getCategory()));
        given(projectDao.findById(1)).willReturn(Optional.ofNullable(getProject()));
        given(personDao.findById(1)).willReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundPersonException.class, () -> {
            noteService.saveNote(noteDto);
        });
    }

    @Test
    public void shouldReturnNoteWithSetPersonIdAndProjectAndCategory() {
        //given
        given(categoryDao.findById(1)).willReturn(Optional.ofNullable(getCategory()));
        given(projectDao.findById(1)).willReturn(Optional.ofNullable(getProject()));
        given(personDao.findById(1)).willReturn(Optional.ofNullable(getPerson()));
        given(noteDao.save(noteWithIdNull)).willReturn(note);
        //when
        NoteDto noteDto = noteService.saveNote(this.noteDtoWithIdNull);
        //then
        assertThat(noteDto.getPersonId(), is(getPerson().getId()));
        assertThat(noteDto.getCategoryId(), is(getCategory().getId()));
        assertThat(noteDto.getProjectId(), is(getProject().getId()));
    }


    @Test
    public void shouldReturnNoteWithNotSetPersonIdButSetProjectAndCategory() {
        //given
        given(categoryDao.findById(1)).willReturn(Optional.ofNullable(getCategory()));
        given(projectDao.findById(1)).willReturn(Optional.ofNullable(getProject()));
        given(noteDao.save(noteWithOutPerson)).willReturn(noteWithOutPerson);
        //when
        NoteDto noteDto = noteService.saveNote(noteDtoWithOutPerson);
        //then
        assertThat(noteDto.getPersonId(), nullValue());
        assertThat(noteDto.getCategoryId(), is(getCategory().getId()));
        assertThat(noteDto.getProjectId(), is(getProject().getId()));
    }


    // ------------  NoteDto deleteNote(Integer noteId) ---------------

    @Test
    public void shouldReturnDeletedNote() {
        //given
        given(noteDao.findById(1)).willReturn(Optional.ofNullable(note));
        //when
        NoteDto noteDto = noteService.deleteNote(1);
        //then
        assertThat(noteDto.getId(), is(1));
    }

    // -------------- List<NoteDto> getListNoteByCategoryId(Integer categoryId)  ----------

    @Test
    public void shouldReturnAllNoteWithCategoryById() {
        //given
        given(noteDao.findAllByCategoryId(1)).willReturn(getListNote());
        //when
        List<NoteDto> listNoteByCategoryId = noteService.getListNoteByCategoryId(1);
        //then
        assertThat(listNoteByCategoryId.get(0).getCategoryId(), is(1));
    }


    // ----------- NoteDto addOwnerPerson(PersonForNoteDto personForNoteDto)  ------------


    @Test
    public void shouldThrowExceptionIfNoteNotHaveSetProject() {
        //given
        Note noteTwo = getListNote().get(1);
        given(noteDao.findById(2)).willReturn(Optional.ofNullable(noteTwo));
        given(personDao.findById(1)).willReturn(Optional.ofNullable(getPerson()));
        //when
        //then
        assertThrows(NoteNotSetProjectException.class, () -> {
            noteService.addOwnerPerson(personForNoteDto);
        });
    }

    @Test
    public void shouldThrowExceptionIfNoteAndPersonNotInTheSameProject() {
        //given
        Note noteTwo = getListNote().get(1);
        Project projectTwo = new Project();
        projectTwo.setId(2);
        projectTwo.setName("Name");
        noteTwo.setProject(projectTwo);

        given(noteDao.findById(2)).willReturn(Optional.ofNullable(noteTwo));
        given(personDao.findById(1)).willReturn(Optional.ofNullable(person));

        //when
        //then
        assertThrows(ResponseStatusException.class, () -> {
            noteService.addOwnerPerson(personForNoteDto);
        });
    }

    //powinien zwrocic note z ustawionym personem.
    @Test
    public void shouldReturnNoteWithSetNewPerson() {
        //given
        Note noteTwo = getListNote().get(1);
        noteTwo.setProject(getProject());

        given(noteDao.findById(2)).willReturn(Optional.ofNullable(noteTwo));
        given(personDao.findById(1)).willReturn(Optional.ofNullable(person));
        given(noteDao.update(noteTwo)).willReturn(noteTwo);
        //when
        NoteDto noteDto = noteService.addOwnerPerson(personForNoteDto);
        //
        assertThat(noteDto.getId(), is(noteDto.getId()));
        assertThat(noteDto.getPersonId(), is(1));



    }

    // ---- NoteDto getNoteById(Integer noteId)  ----
    @Test
    public void shouldReturnNoteById() {
        //given
        given(noteDao.findById(1)).willReturn(Optional.ofNullable(note));
        //when
        NoteDto noteById = noteService.getNoteById(1);
        //then
        assertThat(noteById.getId(), is(1));
    }





    private List<Note> getListNote() {
        Note note = new Note();
        note.setId(1);
        note.setProject(getProject());
        note.setPerson(getPerson());
        note.setTitle("Zadanie1");
        note.setContent("Opis1");
        note.setCategory(getCategory());
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

    public Category getCategory() {
        Category category = new Category();
        category.setId(1);
        return category;
    }

    private Project getProject() {
        Project project = new Project();
        project.setId(1);
        project.setName("Test");
        return project;
    }

    private Person getPerson() {
        Person person = new Person();
        person.setId(1);
        person.setName("Test");
        person.setLastName("Test");
        return person;
    }
}