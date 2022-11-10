package de.szut.lf8_project.mappers;

import de.szut.lf8_project.dtos.employeeDto.EmployeeDTO;
import de.szut.lf8_project.dtos.employeeDto.GetEmployeeDTO;
import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.services.EmployeeProjectService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmployeeMapper {
    
    private final EmployeeProjectService employeeProjectService;
    
    public EmployeeMapper(EmployeeProjectService employeeProjectService) {
        this.employeeProjectService = employeeProjectService;
    }
    
    public GetEmployeeDTO mapEmployeeDTOToGetEmployeeDTO(EmployeeDTO employeeDTO, ProjectEntity projectEntity) {
        String skillWithinProject = null;
        for (EmployeeProjectEntity employeeProjectEntity : employeeProjectService.readAllByEmployeeId(employeeDTO.getId())) {
            if (Objects.equals(employeeProjectEntity.getProjectEntity().getPid(), projectEntity.getPid())) {
                skillWithinProject = employeeProjectEntity.getSkillWithinProject();
            }
        }

        return new GetEmployeeDTO(
                employeeDTO.getId(),
                employeeDTO.getFirstName() + " " + employeeDTO.getLastName(),
                employeeDTO.getSkillSet(),
                skillWithinProject
        );
    }
}
