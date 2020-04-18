package com.example.kanban.exception.note;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The note has no set project, you can't set person for note")
public class NoteNotSetProjectException extends RuntimeException{
}
