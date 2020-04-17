package com.example.kanban.dao.note;

import com.example.kanban.model.Note;

import java.util.List;
import java.util.Optional;

public interface NoteDao {
    Note save(Note note);
    Optional<Note> findById(Integer noteId);
    Note update(Note note);
    void remove(Note note);
    List<Note> findAll();

    List<Note> findAllByCategoryId(Integer categoryId);
}
