package de.szut.lf8_project.repositories;


import de.szut.lf8_project.entities.HelloEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelloRepository extends JpaRepository<HelloEntity, Long> {
    List<HelloEntity> findByMessage(String message);
}
