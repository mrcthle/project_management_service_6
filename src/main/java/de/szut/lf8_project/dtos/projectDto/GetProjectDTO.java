package de.szut.lf8_project.dtos.projectDto;

import de.szut.lf8_project.entities.EmployeeProjectEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
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
