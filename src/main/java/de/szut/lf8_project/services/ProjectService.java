package de.szut.lf8_project.services;

import de.szut.lf8_project.dtos.employeeDto.EmployeeDTO;
import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.entities.ProjectQualificationEntity;
import de.szut.lf8_project.exceptionHandling.*;
import de.szut.lf8_project.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
        checkStartEndDates(projectEntity);
        customerService.getCustomerById(projectEntity.getCustomerId());
        employeeService.getEmployee(projectEntity.getProjectLeader());

        Set<EmployeeProjectEntity> employeeProjectEntities = projectEntity.getProjectEmployees();
        for (EmployeeProjectEntity employeeProjectEntity : employeeProjectEntities) {
            EmployeeDTO employeeDTO = employeeService.getEmployee(employeeProjectEntity.getEmployeeId());
            checkEmployeeQualification(projectEntity, employeeDTO);
            checkEmployeeAvailability(projectEntity, employeeDTO.getId());
        }
        projectEntity = repository.save(projectEntity);
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
        checkStartEndDates(newEntity);
        ProjectEntity entityToUpdate = readById(newEntity.getPid());
        entityToUpdate.setPid(newEntity.getPid());
        entityToUpdate.setDescription(newEntity.getDescription());
        entityToUpdate.setCustomerId(newEntity.getCustomerId());
        entityToUpdate.setComment(newEntity.getComment());
        entityToUpdate.setProjectLeader(newEntity.getProjectLeader());
        entityToUpdate.setStartDate(newEntity.getStartDate());
        entityToUpdate.setPlannedEndDate(newEntity.getPlannedEndDate());
        entityToUpdate.setEndDate(newEntity.getEndDate());
        
        for (EmployeeProjectEntity newEmployeeProjectEntity : newEntity.getProjectEmployees()) {
            EmployeeDTO employeeDTO = employeeService.getEmployee(newEmployeeProjectEntity.getEmployeeId());
            checkEmployeeQualification(newEntity, employeeDTO);
            checkEmployeeAvailability(newEntity, employeeDTO.getId());
            for (EmployeeProjectEntity existingEmployeeProjectEntity : entityToUpdate.getProjectEmployees()) {
                if (Objects.equals(existingEmployeeProjectEntity.getEmployeeId(), employeeDTO.getId())) {
                    newEmployeeProjectEntity.setId(existingEmployeeProjectEntity.getId());
                    break;
                }
            }
        }
        entityToUpdate.setProjectEmployees(newEntity.getProjectEmployees());
        
        for (ProjectQualificationEntity newProjectQualificationEntity : newEntity.getProjectQualifications()) {
            for (ProjectQualificationEntity existingProjectQualificationEntity: entityToUpdate.getProjectQualifications()) {
                if (existingProjectQualificationEntity.getQualification().equals(newProjectQualificationEntity.getQualification())) {
                    newProjectQualificationEntity.setId(existingProjectQualificationEntity.getId());
                    break;
                }
            }
        }
        entityToUpdate.setProjectQualifications(newEntity.getProjectQualifications());
        entityToUpdate = repository.save(entityToUpdate);
        
        checkProjectQualificationEntities(newEntity);
        checkEmployeeProjectEntities(newEntity);
        return entityToUpdate;
    }
    
    public ProjectEntity delete(Long id) {
        ProjectEntity projectEntity = readById(id);
        repository.deleteById(id);
        return projectEntity;
    }
    
    private void checkEmployeeQualification(ProjectEntity projectEntity, EmployeeDTO employeeDTO) {
        String skillSetWithinProject = null;
        for (EmployeeProjectEntity employeeProjectEntity : projectEntity.getProjectEmployees()) {
            skillSetWithinProject = employeeProjectEntity.getSkillWithinProject();
            
            if (!employeeService.getEmployee(employeeProjectEntity.getEmployeeId()).getSkillSet().contains(skillSetWithinProject)) {
                throw new SkillSetNotFoundException("Employee with id = " + employeeDTO.getId() + " does not have the skill " + skillSetWithinProject);
            }
        }
        if (skillSetWithinProject != null) {
            boolean containsSkillSet = false;
            for (ProjectQualificationEntity projectQualificationEntity : projectEntity.getProjectQualifications()) {
                if (projectQualificationEntity.getQualification().equals(skillSetWithinProject)) {
                    containsSkillSet = true;
                    break;
                }
            }
            if (!containsSkillSet) {
                throw new SkillSetNotNeededException(
                        "Project with id = " + projectEntity.getPid() + 
                        " does not need Employee with id = " + employeeDTO.getId() + 
                        " for Skill '" + skillSetWithinProject + "'."
                );
            }
        }
        
        
        for (ProjectQualificationEntity projectQualification : projectEntity.getProjectQualifications()) {
            boolean isQualified = false;
            for (String skillSet : employeeDTO.getSkillSet()) {
                if (skillSet.equals(projectQualification.getQualification())) {
                    isQualified = true;
                    break;
                }
            }
            if (!isQualified) {
                throw new SkillSetNotFoundException(
                        "Employee with id = " + employeeDTO.getId() + " does not have the needed skill '" + projectQualification.getQualification() + "'."
                );
            }
        }
    }
    
    private void checkEmployeeAvailability(final ProjectEntity projectEntity, final Long employeeId) {
        List<EmployeeProjectEntity> employeeProjectMappings = employeeProjectService.readAllByEmployeeId(employeeId);
        for (EmployeeProjectEntity employeeProject : employeeProjectMappings) {
            if (Objects.equals(employeeProject.getProjectEntity().getPid(), projectEntity.getPid())) {
                continue;
            }
            ProjectEntity existingProject = readById(employeeProject.getProjectEntity().getPid());
            if (
                    dateCollides(projectEntity.getStartDate(), existingProject) ||
                    dateCollides(projectEntity.getPlannedEndDate(), existingProject) ||
                    dateCollides(projectEntity.getEndDate(), existingProject) ||
                    projectSurroundsExisting(projectEntity, existingProject)
            ) {
                throw new EmployeeNotAvailableException("Employee with id = " + employeeId + " is blocked by project with id = " + existingProject.getPid());
            }
        }
    }
    
    private boolean dateCollides(LocalDateTime newDate, ProjectEntity existingProject) {
        if (newDate == null) {
            return false;
        }
        if (
                newDate.isAfter(existingProject.getStartDate()) && newDate.isBefore(existingProject.getPlannedEndDate()) ||
                newDate.isEqual(existingProject.getStartDate()) ||
                newDate.isEqual(existingProject.getPlannedEndDate())
        ) {
            return true;
        }
        if (existingProject.getEndDate() != null) {
            if (
                    newDate.isAfter(existingProject.getStartDate()) && newDate.isBefore(existingProject.getEndDate()) ||
                    newDate.isEqual(existingProject.getEndDate())        
            ) {
                return true;
            }
        }
        return false;
    }
    
    private boolean projectSurroundsExisting(ProjectEntity newProject, ProjectEntity existingProject) {
        if (newProject.getStartDate().isBefore(existingProject.getStartDate()) && newProject.getPlannedEndDate().isAfter(existingProject.getPlannedEndDate())) {
            return true;
        }
        if (existingProject.getEndDate() != null) {
            if (newProject.getStartDate().isBefore(existingProject.getStartDate()) && newProject.getPlannedEndDate().isAfter(existingProject.getEndDate())) {
                return true;
            }
            if (newProject.getEndDate() != null) {
                if (
                        newProject.getStartDate().isBefore(existingProject.getStartDate()) && newProject.getEndDate().isAfter(existingProject.getPlannedEndDate()) ||
                        newProject.getStartDate().isBefore(existingProject.getStartDate()) && newProject.getEndDate().isAfter(existingProject.getEndDate())
                ) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void checkStartEndDates(ProjectEntity projectEntity) {
        if ((projectEntity.getPlannedEndDate().isBefore(projectEntity.getStartDate()) ||
                projectEntity.getPlannedEndDate().isEqual(projectEntity.getStartDate())) &&
                (!projectEntity.getDescription().contains("time machine"))
        ) {
            throw new TimeMachineException("If your project is not a time machine, there is no way the planned end date is before the start date.");
        }
        if (projectEntity.getEndDate() != null) {
            if ((projectEntity.getEndDate().isBefore(projectEntity.getStartDate()) ||
                    projectEntity.getEndDate().isEqual(projectEntity.getStartDate())) && 
                    !projectEntity.getDescription().contains("time machine")
            ) {
                throw new TimeMachineException("If your project is not a time machine, there is no way the end date is before the start date.");
            }
        } 
    }
    
    private void checkEmployeeProjectEntities(ProjectEntity projectEntity) {
        for (EmployeeProjectEntity employeeProjectEntity : employeeProjectService.readAllByProjectEntity(projectEntity)) {
            if (!projectEntity.getProjectEmployees().contains(employeeProjectEntity)) {
                employeeProjectService.deleteById(employeeProjectEntity.getId());
            }
        }
    }

    private void checkProjectQualificationEntities(ProjectEntity projectEntity) {
        for (ProjectQualificationEntity projectQualification : projectQualificationService.readAllByProjectEntity(projectEntity)) {
            if (!projectEntity.getProjectQualifications().contains(projectQualification)) {
                projectQualificationService.deleteById(projectQualification.getId());
            }
        }
    }
}
