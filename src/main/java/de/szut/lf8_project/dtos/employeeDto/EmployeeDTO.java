package de.szut.lf8_project.dtos.employeeDto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeDTO {
    
    private Long id;
    
    private String lastName;
    
    private String firstName;
    
    private List<String> skillSet;
}
