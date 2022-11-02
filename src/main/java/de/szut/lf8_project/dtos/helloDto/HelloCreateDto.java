package de.szut.lf8_project.dtos.helloDto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class HelloCreateDto {

    @Size(min = 3, message = "at least length of 3")
    private String message;

    @JsonCreator
    public HelloCreateDto(String message) {
        this.message = message;
    }
}
