package com.example.kanban.exception.person;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Not found person with set id")
public class NotFoundPersonException extends RuntimeException{
    public NotFoundPersonException(String message) {
        super(message);
    }
}
