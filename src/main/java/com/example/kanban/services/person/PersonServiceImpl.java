package com.example.kanban.services.person;


import com.example.kanban.dao.person.PersonDao;
import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonDto;
import com.example.kanban.dto.PersonForProjectDto;
import com.example.kanban.dto.ProjectDto;
import com.example.kanban.exception.person.NotFoundPersonException;
import com.example.kanban.mapper.NoteMapper;
import com.example.kanban.mapper.PersonMapper;
import com.example.kanban.mapper.ProjectMapper;
import com.example.kanban.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService{


    private PersonMapper personMapper;
    private ProjectMapper projectMapper;
    private PersonDao personDao;

    @Autowired
    public PersonServiceImpl( PersonDao personDao) {
        this.personDao = personDao;
        personMapper = new PersonMapper();
        projectMapper = new ProjectMapper();
    }

    @Override
    public List<PersonDto> getListPerson() {
        return personDao
                .findAll()
                .stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public PersonDto updatePerson(PersonDto personDto) {
        getPerson(personDto.getId());
        return mapAndUpdate(personDto);
    }

    @Override
    public PersonDto savePerson(PersonDto personDto) {
        return mapAndSave(personDto);
    }

    @Override
    public PersonDto deletePerson(Integer personId) {
        Person person = getPerson(personId);
        personDao.remove(person);
        return personMapper.toDto(person);
    }

    @Override
    public List<NoteDto> getListNoteFromPerson(Integer personId) {
        Person person = getPerson(personId);

        return person.getNotes()
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoteDto> getListNoteFromPersonForProject(PersonForProjectDto personForProjectDto) {
        Integer personId = personForProjectDto.getPersonId();
        Integer projectId = personForProjectDto.getProjectId();

        return  getPerson(personId)
                .getNotes()
                .stream()
                .filter(note -> note.getProject().getId().equals(projectId))
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());

    }


    @Override
    public PersonDto getPersonById(Integer personId) {
       return personMapper.toDto(getPerson(personId));
    }


    @Override
    public List<ProjectDto> getPersonProjectList(Integer personId) {
        return getPerson(personId)
                .getProjects()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    private Person getPerson(Integer personId) {
        Optional<Person> personById = personDao.findById(personId);
        personById.orElseThrow(() -> {
            throw new NotFoundPersonException("Not found person by id: " + personId);
        });
        return personById.get();
    }

    private PersonDto mapAndSave(PersonDto personDto) {
        Person person = personMapper.toEntity(personDto);
        Person savedPerson = personDao.save(person);
        return personMapper.toDto(savedPerson);
    }

    private PersonDto mapAndUpdate(PersonDto personDto) {
        Person person = personMapper.toEntity(personDto);
        Person savedPerson = personDao.update(person);
        return personMapper.toDto(savedPerson);
    }

}
