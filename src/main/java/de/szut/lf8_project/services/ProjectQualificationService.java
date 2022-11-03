package de.szut.lf8_project.services;

import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.entities.ProjectQualificationEntity;
import de.szut.lf8_project.repositories.ProjectQualificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectQualificationService {
    
    private final ProjectQualificationRepository repository;
    private final EmployeeService employeeService;
    
    public ProjectQualificationService(ProjectQualificationRepository repository) {
        this.repository = repository;
        employeeService = EmployeeService.getInstance();
    }
    
    public List<ProjectQualificationEntity> readAllByProjectEntity(ProjectEntity projectEntity) {
        return repository.findAllByProjectEntity(projectEntity);
    }
}
