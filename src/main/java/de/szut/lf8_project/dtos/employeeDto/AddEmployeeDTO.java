package de.szut.lf8_project.dtos.employeeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class AddEmployeeDTO {

    @NotNull 
    private Long id;
    
    @NotBlank 
    private String skillWithinProject;
}
