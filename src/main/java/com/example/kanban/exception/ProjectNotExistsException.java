package com.example.kanban.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Project by id not exists")
public class ProjectNotExistsException extends RuntimeException{
    public ProjectNotExistsException() {
        super("Project by id not exists");
    }
}
