package de.szut.lf8_project.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
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
}
