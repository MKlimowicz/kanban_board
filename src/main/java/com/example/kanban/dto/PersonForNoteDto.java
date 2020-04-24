package com.example.kanban.dto;

import java.util.Objects;

public class PersonForNoteDto {

    private Integer noteId;
    private Integer personId;


    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonForNoteDto that = (PersonForNoteDto) o;
        return Objects.equals(noteId, that.noteId) &&
                Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId, personId);
    }

    @Override
    public String toString() {
        return "PersonForNoteDto{" +
                "noteId=" + noteId +
                ", personId=" + personId +
                '}';
    }
}
