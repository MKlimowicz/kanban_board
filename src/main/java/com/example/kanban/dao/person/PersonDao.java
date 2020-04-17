package com.example.kanban.dao.person;

import com.example.kanban.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonDao {
    Person save(Person person);
    Optional<Person> findById(Integer personId);
    Person update(Person person);
    void remove(Person person);
    List<Person> findAll();
}
