package de.szut.lf8_project.integrationTests.projects;

import de.szut.lf8_project.dtos.projectDto.AddProjectDTO;
import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.entities.ProjectQualificationEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostProjectIT extends AbstractIntegrationTest {
    
    @Test
    void postValidProject() throws Exception {
        
        AddProjectDTO addProjectDTO = new AddProjectDTO(
                "Test",
                2L,
                "Test",
                LocalDateTime.of(2022, 10, 31, 10, 0, 0),
                LocalDateTime.of(2022, 11, 30, 10, 0, 0),
                null,
                11L,
                List.of(10L, 60L),
                List.of("Java", "Angular")
        );
        
        String validProjectContent = 
                "{" + 
                         "\"description\": \"" + addProjectDTO.getDescription() + "\"," + 
                         "\"customerId\": " + addProjectDTO.getCustomerId() + "," +
                         "\"comment\": \"" + addProjectDTO.getComment() +  "\"," +
                         "\"projectLeader\": " + addProjectDTO.getProjectLeader() + "," +
                         "\"startDate\": \"" + addProjectDTO.getStartDate() + "\"," +
                         "\"plannedEndDate\": \"" + addProjectDTO.getPlannedEndDate() + "\"," +
                         "\"endDate\": \"\"," +
                         "\"projectEmployeeIds\": " + addProjectDTO.getProjectEmployeeIds() + "," +
                         "\"qualifications\": [\"" + addProjectDTO.getQualifications().get(0) + "\",\"" + addProjectDTO.getQualifications().get(1) + "\"]" +
                "}";
        
        final var contentAsString = this.mockMvc.perform(post("/v1/api/pms/project").content(validProjectContent).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("description", is(addProjectDTO.getDescription())))
                .andExpect(jsonPath("customer.id", is(addProjectDTO.getCustomerId().intValue())))
                .andExpect(jsonPath("comment", is(addProjectDTO.getComment())))
                .andExpect(jsonPath("projectLeader.id", is(addProjectDTO.getProjectLeader().intValue())))
                .andExpect(jsonPath("startDate", containsString(addProjectDTO.getStartDate().toString())))
                .andExpect(jsonPath("plannedEndDate", containsString(addProjectDTO.getPlannedEndDate().toString())))
                .andExpect(jsonPath("endDate", is(addProjectDTO.getEndDate())))
                .andExpect(jsonPath("projectEmployees", hasSize(2)))
                .andExpect(jsonPath("projectEmployees.*.id", hasItems(addProjectDTO.getProjectEmployeeIds().get(0).intValue(), addProjectDTO.getProjectEmployeeIds().get(1).intValue())))
                .andExpect(jsonPath("qualifications", hasSize(2)))
                .andExpect(jsonPath("qualifications.*", hasItems(addProjectDTO.getQualifications().get(0), addProjectDTO.getQualifications().get(1))))
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        final Long id = Long.parseLong(new JSONObject(contentAsString).get("id").toString());//find by Id does not work
        
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
        assertThat(loadedProjectEntityEmployeeIds).hasSameElementsAs(addProjectDTO.getProjectEmployeeIds());
        assertThat(loadedProjectQualifications).hasSameElementsAs(addProjectDTO.getQualifications());
    }

    @Test
    void postTimeMachineProject() throws Exception {

        AddProjectDTO addProjectDTO = new AddProjectDTO(
                "Take time machine for test run",
                2L,
                "Test",
                LocalDateTime.of(2022, 10, 31, 10, 0, 0),
                LocalDateTime.of(2022, 10, 10, 10, 0, 0),
                null,
                11L,
                List.of(10L, 60L),
                List.of("Java", "Angular")
        );

        String validProjectContent =
                "{" +
                        "\"description\": \"" + addProjectDTO.getDescription() + "\"," +
                        "\"customerId\": " + addProjectDTO.getCustomerId() + "," +
                        "\"comment\": \"" + addProjectDTO.getComment() +  "\"," +
                        "\"projectLeader\": " + addProjectDTO.getProjectLeader() + "," +
                        "\"startDate\": \"" + addProjectDTO.getStartDate() + "\"," +
                        "\"plannedEndDate\": \"" + addProjectDTO.getPlannedEndDate() + "\"," +
                        "\"endDate\": \"\"," +
                        "\"projectEmployeeIds\": " + addProjectDTO.getProjectEmployeeIds() + "," +
                        "\"qualifications\": [\"" + addProjectDTO.getQualifications().get(0) + "\",\"" + addProjectDTO.getQualifications().get(1) + "\"]" +
                        "}";

        final var contentAsString = this.mockMvc.perform(post("/v1/api/pms/project").content(validProjectContent).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("description", is(addProjectDTO.getDescription())))
                .andExpect(jsonPath("customer.id", is(addProjectDTO.getCustomerId().intValue())))
                .andExpect(jsonPath("comment", is(addProjectDTO.getComment())))
                .andExpect(jsonPath("projectLeader.id", is(addProjectDTO.getProjectLeader().intValue())))
                .andExpect(jsonPath("startDate", containsString(addProjectDTO.getStartDate().toString())))
                .andExpect(jsonPath("plannedEndDate", containsString(addProjectDTO.getPlannedEndDate().toString())))
                .andExpect(jsonPath("endDate", is(addProjectDTO.getEndDate())))
                .andExpect(jsonPath("projectEmployees", hasSize(2)))
                .andExpect(jsonPath("projectEmployees.*.id", hasItems(addProjectDTO.getProjectEmployeeIds().get(0).intValue(), addProjectDTO.getProjectEmployeeIds().get(1).intValue())))
                .andExpect(jsonPath("qualifications", hasSize(2)))
                .andExpect(jsonPath("qualifications.*", hasItems(addProjectDTO.getQualifications().get(0), addProjectDTO.getQualifications().get(1))))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final Long id = Long.parseLong(new JSONObject(contentAsString).get("id").toString());//find by Id does not work

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
        assertThat(loadedProjectEntityEmployeeIds).hasSameElementsAs(addProjectDTO.getProjectEmployeeIds());
        assertThat(loadedProjectQualifications).hasSameElementsAs(addProjectDTO.getQualifications());
    }
    //Todo: tests for negative cases?
}
