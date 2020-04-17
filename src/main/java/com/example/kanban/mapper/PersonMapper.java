package com.example.kanban.mapper;

import com.example.kanban.dto.PersonDto;
import com.example.kanban.model.Person;

public class PersonMapper implements Mapper<Person, PersonDto> {

    @Override
    public Person toEntity(PersonDto dto) {
        Person entity = new Person();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setLastName(dto.getLastName());
        return entity;
    }

    @Override
    public PersonDto toDto(Person entity) {
        PersonDto dto = new PersonDto();
        dto.setId(entity.getId());
        dto.setLastName(entity.getLastName());
        dto.setName(entity.getName());
        return dto;
    }
}
