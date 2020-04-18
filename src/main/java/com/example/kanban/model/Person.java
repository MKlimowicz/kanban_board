package com.example.kanban.model;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Person {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personId")
    private Integer id;
    private String name;
    private String lastName;
    @OneToMany(mappedBy = "person",  cascade = { CascadeType.MERGE , CascadeType.PERSIST  })
    private List<Note> notes;
    @ManyToMany(mappedBy = "persons", cascade = { CascadeType.MERGE , CascadeType.PERSIST  })
    List<Project> projects;


    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
                Objects.equals(name, person.name) &&
                Objects.equals(lastName, person.lastName) &&
                Objects.equals(notes, person.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, notes);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", notes=" + notes +
                '}';
    }
}
