package de.szut.lf8_project.mappers;

import de.szut.lf8_project.dtos.helloDto.HelloCreateDto;
import de.szut.lf8_project.dtos.helloDto.HelloGetDto;
import de.szut.lf8_project.entities.HelloEntity;
import org.springframework.stereotype.Service;

@Service
public class HelloMapper {

    public HelloGetDto mapToGetDto(HelloEntity entity) {
        return new HelloGetDto(entity.getId(), entity.getMessage());
    }

    public HelloEntity mapCreateDtoToEntity(HelloCreateDto dto) {
        var entity = new HelloEntity();
        entity.setMessage(dto.getMessage());
        return entity;
    }
}
