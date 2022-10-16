package de.szut.lf8_project.services;

import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.repositories.EmployeeProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeProjectService {
    
    private final EmployeeProjectRepository repository;
    private final EmployeeService employeeService;
    
    public EmployeeProjectService(EmployeeProjectRepository repository) {
        this.repository = repository;
        employeeService = EmployeeService.getInstance();
    }
    
    public EmployeeProjectEntity create(EmployeeProjectEntity entity) {
        return repository.save(entity);
    }
    
    public List<EmployeeProjectEntity> readAllByEmployeeId(Long employeeId) {
        if (employeeService.getEmployee(employeeId) == null) {
            return null;
        }
        return repository.findAllByEmployeeId(employeeId);
    }
    
    public List<EmployeeProjectEntity> readAllByProjectEntity(ProjectEntity projectEntity) {
        return repository.findAllByProjectEntity(projectEntity);
    }
    
    public EmployeeProjectEntity readByEmployeeIdAndProjectEntity(Long id, ProjectEntity entity) {
        return repository.readByEmployeeIdAndProjectEntity(id, entity);
    }
    
    public EmployeeProjectEntity deleteByEmployeeIdAndProjectEntity(Long id, ProjectEntity entity) {
        return repository.deleteByEmployeeIdAndProjectEntity(id, entity);
    }

}
