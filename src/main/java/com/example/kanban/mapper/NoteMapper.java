package com.example.kanban.mapper;

import com.example.kanban.dto.NoteDto;
import com.example.kanban.model.Category;
import com.example.kanban.model.Note;
import com.example.kanban.model.Person;

public class NoteMapper {

    public static NoteDto toDto(Note note) {
        NoteDto dto = new NoteDto();

        dto.setId(note.getId());
        dto.setContent(note.getContent());
        dto.setTitle(note.getTitle());

        Category category = note.getCategory();
        dto.setCategoryId(category.getId());

        Integer projectId = note.getProject().getId();
        dto.setProjectId(projectId);

        Person person = note.getPerson();
        if(person != null) {
            dto.setPersonId(person.getId());
        }

        return dto;
    }


    public static Note toEntity(NoteDto noteDto) {

        Note note = new Note();

        note.setId(noteDto.getId());
        note.setContent(noteDto.getContent());
        note.setTitle(noteDto.getTitle());

        Category category = new Category();
        category.setId(noteDto.getCategoryId());





        note.setCategory(category);



        return note;
    }
}
