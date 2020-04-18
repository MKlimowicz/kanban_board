package com.example.kanban.services.note;

import com.example.kanban.dto.NoteDto;
import com.example.kanban.dto.PersonForNoteDto;
import com.example.kanban.dto.PersonForProjectDto;
import com.example.kanban.model.Note;

import java.util.List;

public interface NoteService {

    List<NoteDto> getListNote();
    NoteDto updateNote(NoteDto noteDto);
    NoteDto saveNote(NoteDto note);
    NoteDto deleteNote(Integer noteId);
    List<NoteDto> getListNoteByCategoryId(Integer categoryId);
    NoteDto addOwnerPerson(PersonForNoteDto personForNoteDto);
}
