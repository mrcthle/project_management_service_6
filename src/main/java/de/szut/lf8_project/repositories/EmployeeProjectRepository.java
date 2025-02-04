package de.szut.lf8_project.repositories;

import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeProjectEntity, Long> {
    
    List<EmployeeProjectEntity> findAllByEmployeeId(Long employeeId);

    List<EmployeeProjectEntity> findAllByProjectEntity(ProjectEntity projectEntity);
}
