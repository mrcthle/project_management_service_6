package de.szut.lf8_project.mappers;

import de.szut.lf8_project.dtos.employeeDto.EmployeeDTO;
import de.szut.lf8_project.dtos.projectDto.AddProjectDTO;
import de.szut.lf8_project.dtos.projectDto.GetProjectDTO;
import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProjectMapper {
    
    public GetProjectDTO mapToGetDto(ProjectEntity projectEntity) {
        return new GetProjectDTO(
                projectEntity.getId(),
                projectEntity.getDescription(),
                projectEntity.getCustomerId(),
                projectEntity.getComment(),
                projectEntity.getStartDate(),
                projectEntity.getPlannedEndDate(),
                projectEntity.getEndDate(),
                projectEntity.getProjectEmployees()
        );
    }
    
    public ProjectEntity mapAddProjectDtoToEntity(AddProjectDTO addProjectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setDescription(addProjectDTO.getDescription());
        projectEntity.setCustomerId(addProjectDTO.getCustomerId());
        projectEntity.setStartDate(addProjectDTO.getStartDate());
        projectEntity.setPlannedEndDate(addProjectDTO.getPlannedEndDate());
        projectEntity.setEndDate(addProjectDTO.getEndDate());
        projectEntity.setProjectLeader(addProjectDTO.getProjectLeader());
        Set<EmployeeProjectEntity> employeeProjectEntities = new HashSet<>();
        for (EmployeeDTO employeeDTO : addProjectDTO.getProjectEmployees()) {
            EmployeeProjectEntity employeeProjectEntity = new EmployeeProjectEntity();
            employeeProjectEntity.setProjectEntity(projectEntity);
            employeeProjectEntity.setEmployeeId(employeeDTO.getId());
            employeeProjectEntities.add(employeeProjectEntity);
        }
        projectEntity.setProjectEmployees(employeeProjectEntities);
        return projectEntity;
    }
}
