package de.szut.lf8_project.mappers;

import de.szut.lf8_project.dtos.projectDto.AddProjectDTO;
import de.szut.lf8_project.dtos.projectDto.GetProjectDTO;
import de.szut.lf8_project.entities.ProjectEntity;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapper {
    
    public GetProjectDTO mapToGetDto(ProjectEntity projectEntity) {
        return new GetProjectDTO(
                projectEntity.getId(),
                projectEntity.getDescription(),
                projectEntity.getCustomerId(),
                projectEntity.getContactCustomerSide(),
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
        projectEntity.setContactCustomerSide(addProjectDTO.getContactCustomerSide());
        projectEntity.setStartDate(addProjectDTO.getStartDate());
        projectEntity.setPlannedEndDate(addProjectDTO.getPlannedEndDate());
        projectEntity.setEndDate(addProjectDTO.getEndDate());
        projectEntity.setProjectEmployees(addProjectDTO.getProjectEmployees());
        return projectEntity;
    }
}
