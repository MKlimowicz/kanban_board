package com.example.kanban.services.person;

import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonDto;
import com.example.kanban.dto.PersonForProjectDto;
import com.example.kanban.dto.ProjectDto;
import org.aspectj.weaver.ast.Not;

import java.util.List;

public interface PersonService {
    List<PersonDto> getListPerson();

    PersonDto updatePerson(PersonDto personDto);

    PersonDto savePerson(PersonDto personDto);

    PersonDto deletePerson(Integer personId);

    List<NoteDto> getListNoteFromPerson(Integer personId);

    List<NoteDto> getListNoteFromPersonForProject(PersonForProjectDto personForProjectDto);

    PersonDto getPersonById(Integer personId);

    List<ProjectDto> getPersonProjectList(Integer personId);


}
