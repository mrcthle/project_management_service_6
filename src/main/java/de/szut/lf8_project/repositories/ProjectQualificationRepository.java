package de.szut.lf8_project.repositories;

import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.entities.ProjectQualificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectQualificationRepository extends JpaRepository<ProjectQualificationEntity, Long> {
    
    List<ProjectQualificationEntity> findAllByProjectEntity(ProjectEntity projectEntity);
}
