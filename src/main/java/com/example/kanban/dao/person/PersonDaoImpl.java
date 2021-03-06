package com.example.kanban.dao.person;

import com.example.kanban.model.Note;
import com.example.kanban.model.Person;
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
public class PersonDaoImpl implements PersonDao {

    private EntityManager entityManager;

    @Autowired
    public PersonDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Person save(Person person) {
        entityManager.persist(person);
        entityManager.flush();
        return person;
    }

    @Override
    public Optional<Person> findById(Integer personId) {
        Person person = entityManager.find(Person.class, personId);
        return Optional.ofNullable(person);
    }

    @Override
    public Person update(Person person) {
        entityManager.merge(person);
        entityManager.flush();
        return person;
    }

    @Override
    public void remove(Person person) {
        List<Note> notes = person.getNotes();
        List<Project> projects = person.getProjects();

        if (!notes.isEmpty()) {
            notes.forEach(note -> note.setPerson(null));
        }

        if (!projects.isEmpty()) {
            projects.forEach(project -> project.getPersons().remove(person));
        }

        entityManager.remove(person);
    }

    @Override
    public List<Person> findAll() {
        TypedQuery<Person> query = entityManager
                .createQuery(
                        "SELECT e FROM Person e",
                        Person.class
                );
        return query.getResultList();
    }
}
