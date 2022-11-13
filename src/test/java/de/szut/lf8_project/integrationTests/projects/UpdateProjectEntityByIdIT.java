package de.szut.lf8_project.integrationTests.projects;

import de.szut.lf8_project.dtos.employeeDto.AddEmployeeDTO;
import de.szut.lf8_project.dtos.projectDto.AddProjectDTO;
import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.entities.ProjectQualificationEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateProjectEntityByIdIT extends AbstractIntegrationTest {
    
    @Test
    void updateProjectById() throws Exception {
        
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
        
        AddProjectDTO addProjectDTO = new AddProjectDTO(
                "time machine",
                1L,
                "test comment",
                LocalDateTime.of(2022, 5, 31, 10, 0, 0),
                LocalDateTime.of(2022, 4, 30, 10, 0,0),
                LocalDateTime.of(2022, 4, 30, 10, 30,0),
                12L,
                List.of(addEmployeeOne),
                List.of("Java")
        );
        
        String addEmployeeDTOs = "[{\"id\":" + addEmployeeOne.id() + ", \"skillWithinProject\":\"" + addEmployeeOne.skillWithinProject() + "\"}]";
        
        String validProjectContent = "{" +
                "\"description\": \"" + addProjectDTO.getDescription() + "\"," +
                "\"customerId\": " + addProjectDTO.getCustomerId() + "," +
                "\"comment\": \"" + addProjectDTO.getComment() +  "\"," +
                "\"projectLeader\": " + addProjectDTO.getProjectLeader().intValue() + "," +
                "\"startDate\": \"" + addProjectDTO.getStartDate() + "\"," +
                "\"plannedEndDate\": \"" + addProjectDTO.getPlannedEndDate() + "\"," +
                "\"endDate\": \"" + addProjectDTO.getEndDate() + "\"," +
                "\"addEmployeeDTOs\": " + addEmployeeDTOs + "," +
                "\"qualifications\": [\"" + addProjectDTO.getQualifications().get(0) + "\"]" + 
                "}";

        String contentAsString = this.mockMvc.perform(put("/v1/api/pms/project/update/" + projectEntity.getPid()).content(validProjectContent).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("description", is(addProjectDTO.getDescription())))
                .andExpect(jsonPath("customer.id", is(addProjectDTO.getCustomerId().intValue())))
                .andExpect(jsonPath("comment", is(addProjectDTO.getComment())))
                .andExpect(jsonPath("projectLeader.id", is(addProjectDTO.getProjectLeader().intValue())))
                .andExpect(jsonPath("startDate", containsString(addProjectDTO.getStartDate().toString())))
                .andExpect(jsonPath("plannedEndDate", containsString(addProjectDTO.getPlannedEndDate().toString())))
                .andExpect(jsonPath("endDate", containsString(addProjectDTO.getEndDate().toString())))
                .andExpect(jsonPath("projectEmployees", hasSize(1)))
                .andExpect(jsonPath("projectEmployees.*.id", hasItems(addEmployeeOne.id().intValue())))
                .andExpect(jsonPath("qualifications", hasSize(1)))
                .andExpect(jsonPath("qualifications.*", hasItems(addProjectDTO.getQualifications().get(0))))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = Long.parseLong(new JSONObject(contentAsString).get("id").toString());
        final Optional<ProjectEntity> loadedProjectEntity = projectRepository.findById(id);

        if (loadedProjectEntity.isEmpty()) {
            fail();
        }
        
        List<Long> loadedProjectEntityEmployeeIds = new ArrayList<>();
        for (EmployeeProjectEntity employeeProjectEntity : loadedProjectEntity.get().getProjectEmployees()) {
            loadedProjectEntityEmployeeIds.add(employeeProjectEntity.getEmployeeId());
        }
        List<String> loadedProjectQualifications = new ArrayList<>();
        for (ProjectQualificationEntity projectQualification : loadedProjectEntity.get().getProjectQualifications()) {
            loadedProjectQualifications.add(projectQualification.getQualification());
        }
        assertThat(loadedProjectEntity.get().getDescription()).isEqualTo(addProjectDTO.getDescription());
        assertThat(loadedProjectEntity.get().getCustomerId()).isEqualTo(addProjectDTO.getCustomerId());
        assertThat(loadedProjectEntity.get().getComment()).isEqualTo(addProjectDTO.getComment());
        assertThat(loadedProjectEntity.get().getProjectLeader()).isEqualTo(addProjectDTO.getProjectLeader());
        assertThat(loadedProjectEntity.get().getStartDate()).isEqualTo(addProjectDTO.getStartDate());
        assertThat(loadedProjectEntity.get().getPlannedEndDate()).isEqualTo(addProjectDTO.getPlannedEndDate());
        assertThat(loadedProjectEntity.get().getEndDate()).isEqualTo(addProjectDTO.getEndDate());
        assertThat(loadedProjectEntityEmployeeIds).hasSameElementsAs(List.of(addEmployeeOne.id()));
        assertThat(loadedProjectQualifications).hasSameElementsAs(addProjectDTO.getQualifications());
    }
}
