package de.szut.lf8_project.dtos.projectDto;

import de.szut.lf8_project.dtos.customerDto.CustomerDTO;
import de.szut.lf8_project.dtos.employeeDto.EmployeeDTO;
import de.szut.lf8_project.dtos.employeeDto.GetEmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetProjectDTO {
    
    private Long id;
    private String description;
    private CustomerDTO customer;
    private String comment;
    private EmployeeDTO projectLeader;
    private LocalDateTime startDate;
    private LocalDateTime plannedEndDate;
    private LocalDateTime endDate;
    private List<GetEmployeeDTO> projectEmployees;
    private List<String> qualifications;
}
