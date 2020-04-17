package com.example.kanban.dao.project;


import com.example.kanban.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public class ProjectDaoImpl implements ProjectDao {

    private EntityManager em;


    @Autowired
    public ProjectDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Project> findAll() {
        TypedQuery<Project> query = em.createQuery("SELECT p FROM Project p", Project.class);
        return query.getResultList();
    }

    @Override
    public Project update(Project project) {
        em.merge(project);
        em.flush();
        return project;
    }

    @Override
    public Project save(Project project) {
        em.persist(project);
        em.flush();
        return project;
    }

    @Override
    public void remove(Project project) {
        em.remove(project);

    }

    @Override
    public Optional<Project> findById(Integer projectId) {
        Project project = em.find(Project.class, projectId);
        return Optional.ofNullable(project);
    }

    @Override
    public Optional<Project> findByName(String name) {
        Project project = em.find(Project.class, name);
        return Optional.ofNullable(project);
    }
}
