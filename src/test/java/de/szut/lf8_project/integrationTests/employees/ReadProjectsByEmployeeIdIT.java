package de.szut.lf8_project.integrationTests.employees;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReadProjectsByEmployeeIdIT extends AbstractIntegrationTest {
    
    @Test
    void readAllProjectsByEmployeeId() throws Exception {

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

        this.mockMvc.perform(get("/v1/api/pms/employee/" + employeeProjectEntity1.getEmployeeId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(projectEntity.getPid().intValue())))
                .andExpect(jsonPath("$[0].description", is(projectEntity.getDescription())))
                .andExpect(jsonPath("$[0].customer.id", is(projectEntity.getCustomerId().intValue())))
                .andExpect(jsonPath("$[0].comment", is(projectEntity.getComment())))
                .andExpect(jsonPath("$[0].startDate", containsString(projectEntity.getStartDate().toString())))
                .andExpect(jsonPath("$[0].plannedEndDate", containsString(projectEntity.getPlannedEndDate().toString())))
                .andExpect(jsonPath("$[0].endDate", is(projectEntity.getEndDate())))
                .andExpect(jsonPath("$[0].projectEmployees", hasSize(2)))
                .andExpect(jsonPath("$[0].projectEmployees.*.id",
                        hasItems(
                                ((EmployeeProjectEntity) projectEntity.getProjectEmployees().toArray()[0]).getEmployeeId().intValue(),
                                ((EmployeeProjectEntity) projectEntity.getProjectEmployees().toArray()[1]).getEmployeeId().intValue())
                ))
                .andExpect(jsonPath("$[0].qualifications", hasSize(2)))
                .andExpect(jsonPath("$[0].qualifications.*",
                        hasItems(
                                ((ProjectQualificationEntity) projectEntity.getProjectQualifications().toArray()[0]).getQualification(),
                                ((ProjectQualificationEntity) projectEntity.getProjectQualifications().toArray()[1]).getQualification())
                ))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
