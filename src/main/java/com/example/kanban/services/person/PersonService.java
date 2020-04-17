package com.example.kanban.services.person;

import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonDto;
import com.example.kanban.dto.ProjectDto;

import java.util.List;

public interface PersonService {
    List<PersonDto> getListPerson();

    PersonDto updatePerson(PersonDto personDto);

    PersonDto savePerson(PersonDto personDto);

    PersonDto deletePerson(Integer personId);

    List<NoteDto> getListNoteForPerson(Integer personId);

    PersonDto getPersonById(Integer personId);

    List<ProjectDto> getPersonProjectList(Integer personId);
}
