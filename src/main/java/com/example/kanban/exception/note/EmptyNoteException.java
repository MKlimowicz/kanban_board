package com.example.kanban.exception.note;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Content or Title or Category name or Category Id is empty")
public class EmptyNoteException extends RuntimeException {
    public EmptyNoteException(String value) {
        super(value);
    }
}
