package de.szut.lf8_project.integrationTests.projects;

import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.entities.ProjectQualificationEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Set;

public class ReadByIdProjectIT extends AbstractIntegrationTest {
    
    @Test
    void readProjectById() throws Exception {

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setDescription("Test");
        projectEntity.setCustomerId(2L);
        projectEntity.setComment("Test");
        projectEntity.setStartDate(LocalDateTime.of(2022, 10, 31, 10, 0, 0));
        projectEntity.setPlannedEndDate(LocalDateTime.of(2022, 11, 30, 10, 0,0 ));
        projectEntity.setEndDate(null);
        projectEntity.setProjectLeader(11L);

        EmployeeProjectEntity employeeProjectEntity1 = new EmployeeProjectEntity();
        employeeProjectEntity1.setProjectEntity(projectEntity);
        employeeProjectEntity1.setEmployeeId(10L);
        EmployeeProjectEntity employeeProjectEntity2 = new EmployeeProjectEntity();
        employeeProjectEntity2.setProjectEntity(projectEntity);
        employeeProjectEntity2.setEmployeeId(60L);

        ProjectQualificationEntity projectQualification1 = new ProjectQualificationEntity();
        projectQualification1.setProjectEntity(projectEntity);
        projectQualification1.setQualification("Java");
        ProjectQualificationEntity projectQualification2 = new ProjectQualificationEntity();
        projectQualification2.setProjectEntity(projectEntity);
        projectQualification2.setQualification("Angular");
        
        projectEntity.setProjectEmployees(Set.of(employeeProjectEntity1, employeeProjectEntity2));
        projectEntity.setProjectQualifications(Set.of(projectQualification1, projectQualification2));
        
        projectEntity = projectRepository.save(projectEntity);
        
        this.mockMvc.perform(get("/v1/api/pms/project/read/" + projectEntity.getPid()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id", is(projectEntity.getPid().intValue())))
                .andExpect(jsonPath("description", is(projectEntity.getDescription())))
                .andExpect(jsonPath("customer.id", is(projectEntity.getCustomerId().intValue())))
                .andExpect(jsonPath("comment", is(projectEntity.getComment())))
                .andExpect(jsonPath("startDate", containsString(projectEntity.getStartDate().toString())))
                .andExpect(jsonPath("plannedEndDate", containsString(projectEntity.getPlannedEndDate().toString())))
                .andExpect(jsonPath("endDate", is(projectEntity.getEndDate())))
                .andExpect(jsonPath("projectEmployees", hasSize(2)))
                .andExpect(jsonPath("projectEmployees.*.id", 
                        hasItems(
                                ((EmployeeProjectEntity) projectEntity.getProjectEmployees().toArray()[0]).getEmployeeId().intValue(), 
                                ((EmployeeProjectEntity) projectEntity.getProjectEmployees().toArray()[1]).getEmployeeId().intValue())
                ))
                .andExpect(jsonPath("qualifications", hasSize(2)))
                .andExpect(jsonPath("qualifications.*", 
                        hasItems(
                                ((ProjectQualificationEntity) projectEntity.getProjectQualifications().toArray()[0]).getQualification(), 
                                ((ProjectQualificationEntity) projectEntity.getProjectQualifications().toArray()[1]).getQualification())
                ))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
