package com.example.kanban.exception.person;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The person is already added to the project")
public class PersonIsAlreadyAddedToProjectException extends RuntimeException {
}
