package de.szut.lf8_project.repositories;

import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.entities.ProjectQualificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectQualificationRepository extends JpaRepository<ProjectQualificationEntity, Long> {
    
    List<ProjectQualificationEntity> findAllByProjectEntity(ProjectEntity projectEntity);
}
