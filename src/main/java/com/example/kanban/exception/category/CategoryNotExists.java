package com.example.kanban.exception.category;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Category with this name not exists")
public class CategoryNotExists extends RuntimeException {
    public CategoryNotExists(String message) {
        super(message);
    }
}
