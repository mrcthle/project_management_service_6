package de.szut.lf8_project.integrationTests.projects;

import de.szut.lf8_project.dtos.employeeDto.AddEmployeeDTO;
import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.entities.ProjectQualificationEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteEmployeeFromProjectByProjectIdIT extends AbstractIntegrationTest {
    
    @Test
    void deleteEmployeeFromProjectByProjectId() throws Exception {

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setDescription("Test");
        projectEntity.setCustomerId(2L);
        projectEntity.setComment("Test");
        projectEntity.setStartDate(LocalDateTime.of(2022, 10, 31, 10, 0, 0));
        projectEntity.setPlannedEndDate(LocalDateTime.of(2022, 11, 30, 10, 0,0 ));
        projectEntity.setEndDate(null);
        projectEntity.setProjectLeader(11L);

        AddEmployeeDTO addEmployeeOne = new AddEmployeeDTO(10L,"Java");
        AddEmployeeDTO addEmployeeTwo = new AddEmployeeDTO(60L,"Angular");

        EmployeeProjectEntity employeeProjectEntity1 = new EmployeeProjectEntity();
        employeeProjectEntity1.setProjectEntity(projectEntity);
        employeeProjectEntity1.setEmployeeId(addEmployeeOne.id());
        employeeProjectEntity1.setSkillWithinProject(addEmployeeOne.skillWithinProject());
        EmployeeProjectEntity employeeProjectEntity2 = new EmployeeProjectEntity();
        employeeProjectEntity2.setProjectEntity(projectEntity);
        employeeProjectEntity2.setEmployeeId(addEmployeeTwo.id());
        employeeProjectEntity2.setSkillWithinProject(addEmployeeTwo.skillWithinProject());

        ProjectQualificationEntity projectQualification1 = new ProjectQualificationEntity();
        projectQualification1.setProjectEntity(projectEntity);
        projectQualification1.setQualification("Java");
        ProjectQualificationEntity projectQualification2 = new ProjectQualificationEntity();
        projectQualification2.setProjectEntity(projectEntity);
        projectQualification2.setQualification("Angular");

        projectEntity.setProjectEmployees(Set.of(employeeProjectEntity1, employeeProjectEntity2));
        projectEntity.setProjectQualifications(Set.of(projectQualification1, projectQualification2));

        projectEntity = projectRepository.save(projectEntity);

        this.mockMvc.perform(delete("/v1/api/pms/project/delete/" + projectEntity.getPid() + "/" + employeeProjectEntity2.getEmployeeId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(projectEntity.getPid().intValue())))
                .andExpect(jsonPath("description", is(projectEntity.getDescription())))
                .andExpect(jsonPath("customer.id", is(projectEntity.getCustomerId().intValue())))
                .andExpect(jsonPath("comment", is(projectEntity.getComment())))
                .andExpect(jsonPath("startDate", containsString(projectEntity.getStartDate().toString())))
                .andExpect(jsonPath("plannedEndDate", containsString(projectEntity.getPlannedEndDate().toString())))
                .andExpect(jsonPath("endDate", is(projectEntity.getEndDate())))
                .andExpect(jsonPath("projectEmployees", hasSize(1)))
                .andExpect(jsonPath("projectEmployees[0].id", is(employeeProjectEntity1.getEmployeeId().intValue())))
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
