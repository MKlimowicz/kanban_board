package com.example.kanban.exception.project;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Project with this name exists, change name project.")
public class ProjectWithThisNameExistsException extends RuntimeException {
    public ProjectWithThisNameExistsException(String s) {
        super(s);
    }
}
