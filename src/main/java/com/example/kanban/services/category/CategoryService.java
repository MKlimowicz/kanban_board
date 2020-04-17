package com.example.kanban.services.category;

import com.example.kanban.dto.NoteDto;
import com.example.kanban.model.Category;
import com.example.kanban.model.Note;

import java.util.List;

public interface CategoryService {
    List<Category> getListCategory();
    Category getCategoryByName(String name);
    Category getCategoryById(Integer id);
}


