package de.szut.lf8_project.services;

import de.szut.lf8_project.dtos.employeeDto.EmployeeDTO;
import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.entities.ProjectQualificationEntity;
import de.szut.lf8_project.exceptionHandling.SkillSetNotFound;
import de.szut.lf8_project.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final EmployeeProjectService employeeProjectService;

    public ProjectService(
            ProjectRepository repository,
            CustomerService customerService,
            EmployeeProjectService employeeProjectService
    ) {
        this.repository = repository;
        this.customerService = customerService;
        this.employeeService = EmployeeService.getInstance();
        this.employeeProjectService = employeeProjectService;
    }

    public ProjectEntity create(ProjectEntity projectEntity) {
        customerService.getCustomerById(projectEntity.getCustomerId());
        employeeService.getEmployee(projectEntity.getProjectLeader());

        Set<EmployeeProjectEntity> employeeProjectEntities = projectEntity.getProjectEmployees();
        for (EmployeeProjectEntity employeeProjectEntity : employeeProjectEntities) {
            EmployeeDTO employeeDTO = employeeService.getEmployee(employeeProjectEntity.getEmployeeId());
            checkEmployeeQualification(projectEntity, employeeDTO);
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
        if (projectEntity.isPresent()) {
            return projectEntity.get();
        }
        return null;
    }

    public List<ProjectEntity> readAll() {
        return repository.findAll();
    }

    public ProjectEntity update(ProjectEntity newEntity) {
        Optional<ProjectEntity> entityToUpdate = repository.findById(newEntity.getPid());
        if (entityToUpdate.isEmpty()) {
            return repository.save(newEntity);
        }
        ProjectEntity entity = entityToUpdate.get();
        entity.setPid(newEntity.getPid());
        entity.setDescription(newEntity.getDescription());
        entity.setCustomerId(newEntity.getCustomerId());
        entity.setComment(newEntity.getComment());
        entity.setStartDate(newEntity.getStartDate());
        entity.setPlannedEndDate(newEntity.getPlannedEndDate());
        entity.setEndDate(newEntity.getEndDate());
        entity.setProjectEmployees(newEntity.getProjectEmployees());
        for (EmployeeProjectEntity employeeProjectEntity : newEntity.getProjectEmployees()) {
            EmployeeDTO employeeDTO = employeeService.getEmployee(employeeProjectEntity.getEmployeeId());
            checkEmployeeQualification(newEntity, employeeDTO);
        }
        entity.setProjectQualifications(newEntity.getProjectQualifications());
        return repository.save(entity);
    }
    
    public ProjectEntity delete(ProjectEntity projectEntity) {
        Optional<ProjectEntity> entity = repository.findById(projectEntity.getId());
        if (entity.isPresent()) {
            repository.delete(projectEntity);
            return entity.get();
        }
        return null;
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
}
