package com.example.kanban.model;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Project {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="projectId")
    private Integer id;
    private String name;

    @ManyToMany()
    @JoinTable(name = "person_project",
            joinColumns = {@JoinColumn(name="projectId", referencedColumnName="projectId")},
            inverseJoinColumns = {@JoinColumn(name="personId", referencedColumnName="personId")}
    )
    private List<Person> persons;

    @OneToMany(mappedBy = "project",
            cascade = {  CascadeType.REMOVE },
            orphanRemoval = true)

    private List<Note> notes;


    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
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

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id) &&
                Objects.equals(name, project.name) &&
                Objects.equals(persons, project.persons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, persons);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", persons=" + persons +
                '}';
    }
}
