package com.example.kanban.restController;


import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonDto;
import com.example.kanban.dto.ProjectDto;
import com.example.kanban.model.Person;
import com.example.kanban.services.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/person")
@CrossOrigin
public class PersonRestController {

    private PersonService personService;

    @Autowired
    public PersonRestController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public List<PersonDto> getListPerson() {
        return personService.getListPerson();
    }


    @PostMapping()
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto personDto) {
        if(personDto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id the person to create cannot bo set");
        }
        PersonDto savedPersonDto = personService.savePerson(personDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPersonDto.getId())
                .toUri();

        return  ResponseEntity.created(location).body(savedPersonDto);
    }


    @PutMapping()
    public ResponseEntity<PersonDto> updatePerson(@RequestBody PersonDto personDto) {
        if(personDto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person to update, must have set id");
        }
        PersonDto updatedPerson = personService.updatePerson(personDto);
        return ResponseEntity.ok(updatedPerson);
    }


    @DeleteMapping("/{personId}")
    public ResponseEntity<PersonDto> deletePerson(@PathVariable Integer personId) {
        if(personId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must give personId to delete.");
        }
        PersonDto deletedPerson = personService.deletePerson(personId);
        return ResponseEntity.ok(deletedPerson);
    }


    @GetMapping("/notes/{personId}")
    public List<NoteDto> getNotesForPerson(@PathVariable Integer personId) {
        if(personId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You will give personId, if you want get response with notes for this person"
            );
        }
        return personService.getListNoteForPerson(personId);
    }


    @GetMapping("/{personId}")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable Integer personId) {
        if(personId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must give personId.");
        }
        PersonDto personDto = personService.getPersonById(personId);
        return ResponseEntity.ok(personDto);
    }


    @GetMapping("/projects/{personId}")
    public List<ProjectDto> getPersonProjectListById(@PathVariable Integer personId) {
        if(personId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must give personId.");
        }
        return personService.getPersonProjectList(personId);
    }

}
