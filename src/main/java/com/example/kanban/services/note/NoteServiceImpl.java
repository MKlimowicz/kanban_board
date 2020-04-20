package com.example.kanban.services.note;


import com.example.kanban.dao.category.CategoryDao;
import com.example.kanban.dao.note.NoteDao;
import com.example.kanban.dao.person.PersonDao;
import com.example.kanban.dao.project.ProjectDao;
import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonForNoteDto;
import com.example.kanban.exception.project.ProjectNotExistsException;
import com.example.kanban.exception.note.EmptyNoteException;
import com.example.kanban.exception.category.NotFoundCategoryException;
import com.example.kanban.exception.note.NotFoundNoteException;
import com.example.kanban.exception.note.NoteNotSetProjectException;
import com.example.kanban.exception.person.NotFoundPersonException;
import com.example.kanban.mapper.NoteMapper;
import com.example.kanban.model.Category;
import com.example.kanban.model.Note;
import com.example.kanban.model.Person;
import com.example.kanban.model.Project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {


    private CategoryDao categoryDao;
    private ProjectDao projectDao;
    private PersonDao personDao;
    private NoteDao noteDao;

    @Autowired
    public NoteServiceImpl(
            CategoryDao categoryDao,
            PersonDao personDao,
            ProjectDao projectDao,
            NoteDao noteDao) {

        this.personDao = personDao;
        this.noteDao = noteDao;
        this.projectDao = projectDao;
        this.categoryDao = categoryDao;
    }


    @Override
    public List<NoteDto> getListNote() {
        return noteDao
                .findAll()
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public NoteDto updateNote(NoteDto noteDto) {
        getNote(noteDto.getId());
        return mapAndUpdate(noteDto);
    }

    @Override
    public NoteDto saveNote(NoteDto dto) {

        if (dto.getContent().isEmpty() || dto.getTitle().isEmpty()) {

            throw new EmptyNoteException(
                    "Note can't have empty value, content: " + dto.getContent() +
                            " title: " + dto.getTitle()
            );
        }

        Note note = new Note();

        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());

        Category category = getCategory(dto.getCategoryId());
        Project project = getProject(dto.getProjectId());

        if (dto.getPersonId() != null) {
            Person person = personDao
                    .findById(dto.getPersonId())
                    .orElseThrow(() -> new NotFoundPersonException("Not found person by id: " + dto.getPersonId()));

            note.setPerson(person);
        }

        note.setProject(project);
        note.setCategory(category);

        return mapAndSave(note);
    }


    @Override
    public NoteDto deleteNote(Integer noteId) {
        Note note = getNote(noteId);
        noteDao.remove(note);
        return NoteMapper.toDto(note);
    }

    @Override
    public List<NoteDto> getListNoteByCategoryId(Integer categoryId) {
        return noteDao
                .findAllByCategoryId(categoryId)
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public NoteDto addOwnerPerson(PersonForNoteDto personForNoteDto) {
        Integer noteId = personForNoteDto.getNoteId();
        Integer personId = personForNoteDto.getPersonId();


        Note noteById = getNote(noteId);
        Person personById = getPerson(personId);


        List<Project> projectsFromPerson = personById.getProjects();
        Project projectWithNote = noteById.getProject();

        if (projectWithNote == null) {
            throw new NoteNotSetProjectException();
        }

        if (!projectsFromPerson.contains(projectWithNote)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Project from person who you try set how owner for note is not assigned to the same project as the note"
            );
        }

        noteById.setPerson(personById);
        return NoteMapper.toDto(noteDao.update(noteById));
    }

    private NoteDto mapAndUpdate(NoteDto noteDto) {
        Note note = NoteMapper.toEntity(noteDto);
        Note savedNote = noteDao.update(note);
        return NoteMapper.toDto(savedNote);
    }

    private NoteDto mapAndSave(Note note) {
        Note savedNote = noteDao.save(note);
        return NoteMapper.toDto(savedNote);
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


    private Note getNote(Integer noteId) {
        return noteDao
                .findById(noteId)
                .orElseThrow(NotFoundNoteException::new);
    }

    private Category getCategory(Integer categoryId) {
        return categoryDao
                .findById(categoryId)
                .orElseThrow(NotFoundCategoryException::new);
    }


}
