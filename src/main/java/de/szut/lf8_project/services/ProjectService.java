package de.szut.lf8_project.services;

import de.szut.lf8_project.dtos.employeeDto.EmployeeDTO;
import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.entities.ProjectQualificationEntity;
import de.szut.lf8_project.exceptionHandling.EmployeeNotAvailableException;
import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_project.exceptionHandling.SkillSetNotFound;
import de.szut.lf8_project.exceptionHandling.TimeMachineException;
import de.szut.lf8_project.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final EmployeeProjectService employeeProjectService;
    private final ProjectQualificationService projectQualificationService;

    public ProjectService(
            ProjectRepository repository,
            CustomerService customerService,
            EmployeeProjectService employeeProjectService,
            ProjectQualificationService projectQualificationService
    ) {
        this.repository = repository;
        this.customerService = customerService;
        this.employeeService = EmployeeService.getInstance();
        this.employeeProjectService = employeeProjectService;
        this.projectQualificationService = projectQualificationService;
    }

    public ProjectEntity create(ProjectEntity projectEntity) {
        if ((projectEntity.getPlannedEndDate().isBefore(projectEntity.getStartDate()) ||
                projectEntity.getEndDate().isBefore(projectEntity.getStartDate()) || 
                projectEntity.getPlannedEndDate().isEqual(projectEntity.getStartDate()) || 
                projectEntity.getEndDate().isEqual(projectEntity.getStartDate())) && 
                (!projectEntity.getDescription().contains("time machine"))
        ) {
            throw new TimeMachineException("If your project is not a time machine, there is no way the end date is before the start date.");
        }
        
        customerService.getCustomerById(projectEntity.getCustomerId());
        employeeService.getEmployee(projectEntity.getProjectLeader());

        Set<EmployeeProjectEntity> employeeProjectEntities = projectEntity.getProjectEmployees();
        for (EmployeeProjectEntity employeeProjectEntity : employeeProjectEntities) {
            EmployeeDTO employeeDTO = employeeService.getEmployee(employeeProjectEntity.getEmployeeId());
            checkEmployeeQualification(projectEntity, employeeDTO);
            checkEmployeeAvailability(projectEntity, employeeDTO.getId());
        }
        //todo: Zeitfenster des Mitarbeiters fehlt
        projectEntity = repository.save(projectEntity);
        for (EmployeeProjectEntity employeeProjectEntity : projectEntity.getProjectEmployees()) {
            employeeProjectService.create(employeeProjectEntity);
        }
        return projectEntity;
    }

    public ProjectEntity readById(Long id) {
        Optional<ProjectEntity> projectEntity = repository.findById(id);
        if (projectEntity.isEmpty()) {
            throw new ResourceNotFoundException("Project with id = " + id + " does not exist.");
        }
        return projectEntity.get();
    }

    public List<ProjectEntity> readAll() {
        return repository.findAll();
    }

    public ProjectEntity update(ProjectEntity newEntity) {
        ProjectEntity entityToUpdate = readById(newEntity.getPid());
        entityToUpdate.setPid(newEntity.getPid());
        entityToUpdate.setDescription(newEntity.getDescription());
        entityToUpdate.setCustomerId(newEntity.getCustomerId());
        entityToUpdate.setComment(newEntity.getComment());
        entityToUpdate.setStartDate(newEntity.getStartDate());
        entityToUpdate.setPlannedEndDate(newEntity.getPlannedEndDate());
        entityToUpdate.setEndDate(newEntity.getEndDate());
        entityToUpdate.setProjectEmployees(newEntity.getProjectEmployees());
        for (EmployeeProjectEntity employeeProjectEntity : newEntity.getProjectEmployees()) {
            EmployeeDTO employeeDTO = employeeService.getEmployee(employeeProjectEntity.getEmployeeId());
            checkEmployeeQualification(newEntity, employeeDTO);
            checkEmployeeAvailability(newEntity, employeeDTO.getId());
        }
        entityToUpdate.setProjectQualifications(newEntity.getProjectQualifications());
        return repository.save(entityToUpdate);
    }
    
    public ProjectEntity delete(Long id) {
        ProjectEntity projectEntity = readById(id);
        repository.deleteById(id);
        return projectEntity;
    }
    
    private void checkEmployeeQualification(ProjectEntity projectEntity, EmployeeDTO employeeDTO) {
        for (ProjectQualificationEntity projectQualification : projectEntity.getProjectQualifications()) {
            boolean isQualified = false;
            for (String skillSet : employeeDTO.getSkillSet()) {
                if (skillSet.equals(projectQualification.getQualification())) {
                    isQualified = true;
                    break;
                }
            }
            if (!isQualified) {
                throw new SkillSetNotFound(
                        "Employee with id = " + employeeDTO.getId() + " does not have the needed skill '" + projectQualification.getQualification() + "'."
                );
            }
        }
    }
    
    private void checkEmployeeAvailability(final ProjectEntity projectEntity, final Long employeeId) {
        List<EmployeeProjectEntity> employeeProjectMappings = employeeProjectService.readAllByEmployeeId(employeeId);
        LocalDateTime projectStart;
        LocalDateTime projectPlannedEnd;
        for (EmployeeProjectEntity employeeProject : employeeProjectMappings) {
            projectStart = readById(employeeProject.getProjectEntity().getPid()).getStartDate();
            projectPlannedEnd = readById(employeeProject.getProjectEntity().getPid()).getPlannedEndDate();
            if (
                    projectEntity.getStartDate().isAfter(projectPlannedEnd) && projectEntity.getStartDate().isBefore(projectPlannedEnd) || 
                    projectEntity.getStartDate().isAfter(projectStart) && projectEntity.getStartDate().isBefore(projectPlannedEnd) ||
                    projectEntity.getStartDate().isBefore(projectStart) && projectEntity.getPlannedEndDate().isAfter(projectPlannedEnd)
            ) { 
                throw new EmployeeNotAvailableException("Employee with id = " + employeeId + " is blocked by project with id = " + projectEntity.getPid());
            }
        }
    }
}
