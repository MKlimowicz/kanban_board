package com.example.kanban.dto;

import java.util.Objects;

public class PersonForProjectDto {

    private Integer projectId;
    private Integer personId;


    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonForProjectDto that = (PersonForProjectDto) o;
        return Objects.equals(projectId, that.projectId) &&
                Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, personId);
    }

    @Override
    public String toString() {
        return "PersonForProjectDto{" +
                "projectId=" + projectId +
                ", personId=" + personId +
                '}';
    }
}
