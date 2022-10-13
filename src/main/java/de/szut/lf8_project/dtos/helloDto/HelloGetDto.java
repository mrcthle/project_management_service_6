package de.szut.lf8_project.dtos.helloDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@Getter
@Setter
public class HelloGetDto {

    private long id;

    private String message;

}

