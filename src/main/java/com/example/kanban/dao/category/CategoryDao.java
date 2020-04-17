package com.example.kanban.dao.category;


import com.example.kanban.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {

    List<Category> getListCategory();
    Optional<Category> findByNameCategory(String nameCategory);
    Optional<Category> findById(Integer id);
}
