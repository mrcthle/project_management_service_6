package de.szut.lf8_project.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "employee_project")
public class EmployeeProjectEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;
    
    @Column(name = "skill_within_project", nullable = false)
    private String skillWithinProject;
    
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.DETACH //Todo: Cascade Type überprüfen
    )
    private ProjectEntity projectEntity;
}
