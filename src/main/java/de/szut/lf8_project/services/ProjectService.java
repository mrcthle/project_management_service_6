package de.szut.lf8_project.services;

import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    
    private ProjectRepository repository;
    
    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }
    
    public ProjectEntity create(ProjectEntity projectEntity) {
        return repository.save(projectEntity);
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
        entity.setContactCustomerSide(newEntity.getContactCustomerSide());
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
