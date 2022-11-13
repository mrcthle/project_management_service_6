package de.szut.lf8_project.mappers;

import de.szut.lf8_project.dtos.employeeDto.AddEmployeeDTO;
import de.szut.lf8_project.dtos.employeeDto.EmployeeDTO;
import de.szut.lf8_project.dtos.employeeDto.GetEmployeeDTO;
import de.szut.lf8_project.dtos.projectDto.AddProjectDTO;
import de.szut.lf8_project.dtos.projectDto.GetProjectDTO;
import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.entities.ProjectQualificationEntity;
import de.szut.lf8_project.services.CustomerService;
import de.szut.lf8_project.services.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProjectMapper {
    
    private final EmployeeMapper employeeMapper;
    private final EmployeeService employeeService;
    private final CustomerService customerService;
    
    public ProjectMapper(EmployeeMapper employeeMapper, EmployeeService employeeService, CustomerService customerService) {
        this.employeeMapper = employeeMapper;
        this.employeeService = employeeService;
        this.customerService = customerService;
    }
    
    public GetProjectDTO mapToGetDto(ProjectEntity projectEntity) {
        EmployeeDTO projectLeader = employeeService.getEmployee(projectEntity.getProjectLeader());
        List<GetEmployeeDTO> employees = new ArrayList<>();
        List<String> qualifications = new ArrayList<>();
        for (EmployeeProjectEntity employeeProjectEntity : projectEntity.getProjectEmployees()) {
            EmployeeDTO employeeDTO = employeeService.getEmployee(employeeProjectEntity.getEmployeeId());
            employees.add(employeeMapper.mapEmployeeDTOToGetEmployeeDTO(employeeDTO, projectEntity));
        }
        for (ProjectQualificationEntity projectQualification : projectEntity.getProjectQualifications()) {
            qualifications.add(projectQualification.getQualification());
        }
        return new GetProjectDTO(
                projectEntity.getPid(),
                projectEntity.getDescription(),
                customerService.getCustomerById(projectEntity.getCustomerId()),
                projectEntity.getComment(),
                projectLeader,
                projectEntity.getStartDate(),
                projectEntity.getPlannedEndDate(),
                projectEntity.getEndDate(),
                employees,
                qualifications
        );
    }
    
    public ProjectEntity mapAddProjectDtoToEntity(AddProjectDTO addProjectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setDescription(addProjectDTO.getDescription());
        projectEntity.setComment(addProjectDTO.getComment());
        projectEntity.setCustomerId(addProjectDTO.getCustomerId());
        projectEntity.setStartDate(addProjectDTO.getStartDate());
        projectEntity.setPlannedEndDate(addProjectDTO.getPlannedEndDate());
        projectEntity.setEndDate(addProjectDTO.getEndDate());
        projectEntity.setProjectLeader(addProjectDTO.getProjectLeader());
        Set<EmployeeProjectEntity> employeeProjectEntities = new HashSet<>();
        if (addProjectDTO.getAddEmployeeDTOs() != null) {
            for (AddEmployeeDTO addEmployeeDTO : addProjectDTO.getAddEmployeeDTOs()) {
                employeeService.getEmployee(addEmployeeDTO.getId());
                EmployeeProjectEntity employeeProjectEntity = new EmployeeProjectEntity();
                employeeProjectEntity.setProjectEntity(projectEntity);
                employeeProjectEntity.setEmployeeId(addEmployeeDTO.getId());
                employeeProjectEntity.setSkillWithinProject(addEmployeeDTO.getSkillWithinProject());
                employeeProjectEntities.add(employeeProjectEntity);
            }
        }
        projectEntity.setProjectEmployees(employeeProjectEntities);
        Set<ProjectQualificationEntity> projectQualificationEntities = new HashSet<>();
        if (addProjectDTO.getQualifications() != null) {
            for (String qualification : addProjectDTO.getQualifications()) {
                ProjectQualificationEntity projectQualification = new ProjectQualificationEntity();
                projectQualification.setProjectEntity(projectEntity);
                projectQualification.setQualification(qualification);
                projectQualificationEntities.add(projectQualification);
            }
        }
        projectEntity.setProjectQualifications(projectQualificationEntities);
        return projectEntity;
    }
}
