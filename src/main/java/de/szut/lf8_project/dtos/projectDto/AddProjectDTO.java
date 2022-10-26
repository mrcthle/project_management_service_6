package de.szut.lf8_project.dtos.projectDto;

import de.szut.lf8_project.entities.EmployeeProjectEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class AddProjectDTO {

    @NotBlank(message = "The description must not be blank")
    private String description;
    
    @NotNull(message = "The customer id must not be null")
    private Long customerId;
    
    @NotBlank(message = "There must be a contact at customer side")
    private String contactCustomerSide;
    
    private String comment;
    private LocalDateTime startDate;
    private LocalDateTime plannedEndDate;
    private LocalDateTime endDate;
    private Set<EmployeeProjectEntity> projectEmployees;
}

