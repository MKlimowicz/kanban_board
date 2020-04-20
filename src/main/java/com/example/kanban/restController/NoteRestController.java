package com.example.kanban.restController;


import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonForNoteDto;
import com.example.kanban.services.note.NoteService;
import com.mysql.cj.fabric.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/note")
@CrossOrigin
public class NoteRestController {


    private NoteService noteService;


    @Autowired
    public NoteRestController(NoteService noteService) {
        this.noteService = noteService;
    }


    @GetMapping()
    public List<NoteDto> getListNote() {
        return noteService.getListNote();
    }


    @PostMapping()
    public ResponseEntity<NoteDto> saveNote(@RequestBody NoteDto dto) {
        checkID(dto.getId(), "Note to save, can't have set id");
        NoteDto savedNote = noteService.saveNote(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedNote.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedNote);
    }


    @GetMapping("/category/{categoryId}")
    public List<NoteDto> getListNoteByCategoryId(@PathVariable Integer categoryId) {
        return noteService.getListNoteByCategoryId(categoryId);
    }


    @DeleteMapping("/{noteId}")
    public ResponseEntity<NoteDto> deleteNoteById(@PathVariable Integer noteId) {
        checkID(noteId, "Id must by set when you try delete note");
        NoteDto deletedNote = noteService.deleteNote(noteId);
        return ResponseEntity.ok(deletedNote);
    }

    @PutMapping()
    public ResponseEntity<NoteDto> updateNote(@RequestBody NoteDto dto) {
        checkID(dto.getId(), "Id must by set when you try update note");
        NoteDto updatedNote = noteService.updateNote(dto);
        return ResponseEntity.ok(updatedNote);
    }


    @PutMapping("/addPerson")
    public ResponseEntity<NoteDto> addOwnerPerson(@RequestBody PersonForNoteDto personForNoteDto) {
        Integer noteId = personForNoteDto.getNoteId();
        Integer personId = personForNoteDto.getPersonId();
        checkBothId(noteId, personId, "You must give id for both");

        NoteDto noteDto = noteService.addOwnerPerson(personForNoteDto);
        return ResponseEntity.ok(noteDto);
    }

    private void checkID(Integer noteId, String message) {
        if (noteId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message + ". ID: " + noteId);
        }
    }

    private void checkBothId(Integer noteId, Integer personId, String message){
        if (noteId == null || personId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message
                    + ". noteId: " + noteId
                    + ". personId: " + personId);
        }

    }
}
