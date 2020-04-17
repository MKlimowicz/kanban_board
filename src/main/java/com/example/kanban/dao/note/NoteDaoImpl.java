package com.example.kanban.dao.note;

import com.example.kanban.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class NoteDaoImpl implements NoteDao {

    private EntityManager em;

    @Autowired
    public NoteDaoImpl(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public Note save(Note note) {
        em.persist(note);
        em.flush();
        return note;
    }

    @Override
    public Optional<Note> findById(Integer noteId) {
        Note note = em.find(Note.class, noteId);
        return Optional.ofNullable(note);
    }

    @Override
    public Note update(Note note) {
        em.merge(note);
        em.flush();
        return note;
    }

    @Override
    public void remove(Note note) {
        em.remove(note);
    }

    @Override
    public List<Note> findAll() {
        TypedQuery<Note> query = em.createQuery("SELECT e FROM Note e", Note.class);
        return query.getResultList();
    }

    @Override
    public List<Note> findAllByCategoryId(Integer categoryId) {
        TypedQuery<Note> query = em.createQuery(
                "SELECT e FROM Note e WHERE e.category.id = :categoryId", Note.class
        );
        return query.setParameter("categoryId", categoryId).getResultList();

    }
}
