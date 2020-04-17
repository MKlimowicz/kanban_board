package com.example.kanban.dto;

import java.util.Objects;

public class NoteDto {

    private Integer id;
    private Integer projectId;
    private Integer personId;
    private String content;
    private String title;
    private Integer categoryId;



    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }




    @Override
    public String toString() {
        return "NoteDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteDto noteDto = (NoteDto) o;
        return Objects.equals(id, noteDto.id) &&
                Objects.equals(content, noteDto.content) &&
                Objects.equals(title, noteDto.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, title);
    }
}
