package de.szut.lf8_project.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "employee_project")
public class EmployeeProjectEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;
    
    @Column(name = "project", nullable = false)
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.DETACH //Todo: Cascade Type überprüfen
    )
    private ProjectEntity projectEntity;
    
    @Column(name = "is_project_leader", nullable = false)
    private boolean isProjectLeader;
}
