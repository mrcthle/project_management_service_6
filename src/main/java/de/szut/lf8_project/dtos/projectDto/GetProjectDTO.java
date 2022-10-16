package de.szut.lf8_project.dtos.projectDto;

import de.szut.lf8_project.entities.EmployeeProjectEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class GetProjectDTO {
    
    private Long id;
    private String description;
    private Long customerId;
    private String contactCustomerSide;
    private String comment;
    private LocalDateTime startDate;
    private LocalDateTime plannedEndDate;
    private LocalDateTime endDate;
    private Set<EmployeeProjectEntity> projectEmployees;
}
