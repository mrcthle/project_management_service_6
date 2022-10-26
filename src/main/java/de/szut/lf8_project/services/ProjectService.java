package de.szut.lf8_project.services;

import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {
    
    private final ProjectRepository repository;
    private final  CustomerService customerService;
    private final EmployeeService employeeService;
    private final EmployeeProjectService employeeProjectService;
    
    public ProjectService(ProjectRepository repository, CustomerService customerService, EmployeeProjectService employeeProjectService) {
        this.repository = repository;
    }
    
    public ProjectEntity create(ProjectEntity projectEntity) {
        customerService.getCustomerById(projectEntity.getCustomerId());
        employeeService.getEmployee(projectEntity.getProjectLeader());
        
        Set<EmployeeProjectEntity> employeeProjectEntities = projectEntity.getProjectEmployees();
        for (EmployeeProjectEntity employeeProjectEntity : employeeProjectEntities) {
            employeeService.getEmployee(employeeProjectEntity.getEmployeeId());
            //Todo: check if employee has needed qualification
        }
        projectEntity = repository.save(projectEntity);
        for (EmployeeProjectEntity employeeProjectEntity : projectEntity.getProjectEmployees()) {
            employeeProjectService.create(employeeProjectEntity);
        }
        return projectEntity;
    }
    
    public ProjectEntity readById(Long id) {
        Optional<ProjectEntity> projectEntity = repository.findById(id);
        return projectEntity.orElse(null);
    }
    
    public List<ProjectEntity> readAll() {
        return repository.findAll();
    }
    
    public ProjectEntity update(ProjectEntity newEntity) {
        Optional<ProjectEntity> entityToUpdate = repository.findById(newEntity.getId());
        if (entityToUpdate.isEmpty()) {
            return repository.save(newEntity);
        }
        ProjectEntity entity = entityToUpdate.get();
        entity.setId(newEntity.getId());
        entity.setDescription(newEntity.getDescription());
        entity.setCustomerId(newEntity.getCustomerId());
        entity.setComment(newEntity.getComment());
        entity.setStartDate(newEntity.getStartDate());
        entity.setPlannedEndDate(newEntity.getPlannedEndDate());
        entity.setEndDate(newEntity.getEndDate());
        entity.setProjectEmployees(newEntity.getProjectEmployees());
        return repository.save(entity);
    }
    
    public ProjectEntity delete(ProjectEntity projectEntity) {
        Optional<ProjectEntity> entity = repository.findById(projectEntity.getId());
        if (entity.isPresent()) {
            repository.delete(projectEntity);
        }
        return entity.orElse(null);
    }
}
