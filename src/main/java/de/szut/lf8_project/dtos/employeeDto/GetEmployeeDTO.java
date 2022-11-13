package de.szut.lf8_project.dtos.employeeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GetEmployeeDTO {
    
    private Long id;
    private String name;
    private List<String> skillSet;
    private String skillWithinProject;
}
