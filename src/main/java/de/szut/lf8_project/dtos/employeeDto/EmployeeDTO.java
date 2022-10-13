package de.szut.lf8_project.dtos.employeeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class EmployeeDTO {
    
    private Long employeeId;
    
    private String name;
    
    private List<String> qualifications;
}
