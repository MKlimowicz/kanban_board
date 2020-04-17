package com.example.kanban.exception.note;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Note with this id not exist")
public class NotFoundNoteException extends RuntimeException {
    public NotFoundNoteException() {
        super("Note with this id not exist");
    }
}
