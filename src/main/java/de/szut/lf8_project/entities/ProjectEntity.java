package de.szut.lf8_project.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String description;
    
    @Column(name = "customer_Id")
    private Long customerId;
    
    @Column(name = "contact_customer_side")
    private String contactCustomerSide;
    
    private String comment;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "planned_end_date")
    private LocalDateTime plannedEndDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @Column(name = "project_employees")
    @OneToMany(
            mappedBy = "projectEntity",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private Set<EmployeeProjectEntity> projectEmployees;
}
