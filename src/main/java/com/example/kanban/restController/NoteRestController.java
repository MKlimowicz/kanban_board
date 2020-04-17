package com.example.kanban.restController;


import com.example.kanban.dto.NoteDto;
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
    public List<NoteDto> getListNote(){
        return noteService.getListNote();
    }


    @PostMapping()
    public ResponseEntity<NoteDto> saveNote(@RequestBody NoteDto dto) {
        if(dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Note to save, can't have set id");
        }
        NoteDto savedNote = noteService.saveNote(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedNote.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedNote);
    }


    @GetMapping("/category/{categoryId}")
    public List<NoteDto> getListNoteByCategoryId(@PathVariable  Integer categoryId) {
        return noteService.getListNoteByCategoryId(categoryId);
    }


    @DeleteMapping("/{categoryId}")
    public ResponseEntity<NoteDto> deleteNoteById(@PathVariable Integer categoryId) {
        if(categoryId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must by set when you try delete note");
        }
        NoteDto deletedNote = noteService.deleteNote(categoryId);
        return ResponseEntity.ok(deletedNote);
    }

    @PutMapping()
    public ResponseEntity<NoteDto> updateNote(@RequestBody NoteDto dto) {
        if(dto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Note to save, must have set id");
        }
        NoteDto updatedNote = noteService.updateNote(dto);
        return ResponseEntity.ok(updatedNote);
    }
}
