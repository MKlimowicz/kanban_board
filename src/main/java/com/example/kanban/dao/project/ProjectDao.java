package com.example.kanban.dao.project;

import com.example.kanban.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectDao {
    List<Project> findAll();
    Project update(Project project);
    Project save(Project project);
    void remove(Project project);
    Optional<Project> findById(Integer projectId);
    Optional<Project> findByName(String name);
}
