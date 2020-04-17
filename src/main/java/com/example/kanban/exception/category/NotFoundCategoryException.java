package com.example.kanban.exception.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Not found category")
public class NotFoundCategoryException extends RuntimeException {
    public NotFoundCategoryException() {
        super("Not found category");
    }
}
