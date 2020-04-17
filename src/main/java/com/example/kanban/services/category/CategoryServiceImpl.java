package com.example.kanban.services.category;


import com.example.kanban.dao.category.CategoryDao;
import com.example.kanban.exception.category.NotFoundCategoryException;
import com.example.kanban.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{


    private CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Category> getListCategory() {
        return categoryDao.getListCategory();
    }

    @Override
    public Category getCategoryByName(String nameCategory) {
        return categoryDao
                .findByNameCategory(nameCategory)
                .orElseThrow(NotFoundCategoryException::new);
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryDao
                .findById(id)
                .orElseThrow(NotFoundCategoryException::new);
    }
}
