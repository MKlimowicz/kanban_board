package com.example.kanban.services.person;


import com.example.kanban.dao.person.PersonDao;
import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonDto;
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
        Optional<Person> personById = personDao.findById(personDto.getId());
        personById.orElseThrow(() -> {
            throw new NotFoundPersonException("Not found person by id: " + personDto.getId());
        });
        return mapAndUpdate(personDto);
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

    @Override
    public PersonDto savePerson(PersonDto personDto) {
        return mapAndSave(personDto);
    }

    @Override
    public PersonDto deletePerson(Integer personId) {
        Optional<Person> personById = personDao.findById(personId);
        Person person = personById.orElseThrow(() -> {
            throw new NotFoundPersonException("Not found person by id: " + personId);
        });


        personDao.remove(person);

        return personMapper.toDto(person);
    }

    @Override
    public List<NoteDto> getListNoteForPerson(Integer personId) {
        Optional<Person> personById = personDao.findById(personId);
        Person person = personById.orElseThrow(() -> {
            throw new NotFoundPersonException("Not found person by id: " + personId);
        });

        return person.getNotes()
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PersonDto getPersonById(Integer personId) {
        Optional<Person> personById = personDao.findById(personId);
        personById.orElseThrow(() -> {
            throw new NotFoundPersonException("Not found person by id: " + personId);
        });

        return personById
                .map(personMapper::toDto)
                .get();
    }

    @Override
    public List<ProjectDto> getPersonProjectList(Integer personId) {
        return personDao
                .findById(personId)
                .orElseThrow(() -> {
                    throw new NotFoundPersonException("Not found person by id: " + personId);
                })
                .getProjects()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }
}
