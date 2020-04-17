package com.example.kanban.dao.category;

import com.example.kanban.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public class CategoryDaoImpl implements CategoryDao {

    private EntityManager em;

    @Autowired
    public CategoryDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Category> getListCategory() {
        TypedQuery<Category> query = em.createQuery("SELECT c FROM Category c", Category.class);
        return query.getResultList();
    }

    @Override
    public Optional<Category> findByNameCategory(String nameCategory) {
        TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c WHERE c.nameCategory = :nameCategory",
                Category.class
        );
        Category category = query
                .setParameter("nameCategory", nameCategory)
                .getSingleResult();
        return Optional.ofNullable(category);
    }

    @Override
    public Optional<Category> findById(Integer categoryId) {
        TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c WHERE c.id = :categoryId",
                Category.class
        );
        Category category = query
                .setParameter("categoryId", categoryId)
                .getSingleResult();

        return Optional.ofNullable(category);
    }
}
