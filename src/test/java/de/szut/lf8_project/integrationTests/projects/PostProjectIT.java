package de.szut.lf8_project.integrationTests.projects;

import de.szut.lf8_project.dtos.employeeDto.AddEmployeeDTO;
import de.szut.lf8_project.dtos.projectDto.AddProjectDTO;
import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.entities.ProjectQualificationEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.*;
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
    
    private AddEmployeeDTO addEmployeeOne;
    private AddEmployeeDTO addEmployeeTwo;
    private AddProjectDTO addProjectDTO;
    private String addEmployeeDTOs;
    
    @BeforeEach
    public void init() {
        addEmployeeOne = new AddEmployeeDTO(10L,"Java");
        addEmployeeTwo = new AddEmployeeDTO(60L,"Angular");
        addProjectDTO = new AddProjectDTO(
                "Test",
                2L,
                "Test",
                LocalDateTime.of(2022, 10, 31, 10, 0, 0),
                LocalDateTime.of(2022, 11, 30, 10, 0, 0),
                null,
                11L,
                List.of(addEmployeeOne, addEmployeeTwo),
                List.of("Java", "Angular")
        );
        addEmployeeDTOs = "[{\"id\":" + addEmployeeOne.getId() + ", \"skillWithinProject\":\"" + addEmployeeOne.getSkillWithinProject() + "\"}," +
                "{\"id\":" + addEmployeeTwo.getId() + ",\"skillWithinProject\":\"" + addEmployeeTwo.getSkillWithinProject() + "\"}]";
    }
    
    @Test
    void postValidProject() throws Exception {
        String validProjectContent = convertProjectDTOToString(addProjectDTO);
        
        String contentAsString = performRequest(validProjectContent);
        Long id = Long.parseLong(new JSONObject(contentAsString).get("id").toString());
        final Optional<ProjectEntity> loadedProjectEntity = projectRepository.findById(id);

        if (loadedProjectEntity.isEmpty()) {
            fail();
        }
        validateLoadedProjectEntity(loadedProjectEntity.get());
    }

    @Test
    void postTimeMachineProject() throws Exception {
        addProjectDTO.setDescription("time machine");
        addProjectDTO.setStartDate(LocalDateTime.of(2022, 10, 31, 10, 0, 0));
        addProjectDTO.setPlannedEndDate(LocalDateTime.of(2022, 10, 30, 10, 0, 0));
        
        String validProjectContent = convertProjectDTOToString(addProjectDTO);
        
        String contentAsString = performRequest(validProjectContent);
        Long id = Long.parseLong(new JSONObject(contentAsString).get("id").toString());

        final Optional<ProjectEntity> loadedProjectEntity = projectRepository.findById(id);
        if (loadedProjectEntity.isEmpty()) {
            fail();
        }
        validateLoadedProjectEntity(loadedProjectEntity.get());
    }
    
    @Test
    void postInvalidProjectDueToDescription() throws Exception {
        addProjectDTO.setDescription("");
        
        String validateProjectContent = convertProjectDTOToString(addProjectDTO);
        String contentAsString = performRequestFail(validateProjectContent);
        if (!"The description must not be blank\n".equals(new JSONObject(contentAsString).get("message").toString())) {
            fail();
        }
    }

    @Test
    void postInvalidProjectDueToCustomerId() throws Exception {
        addProjectDTO.setCustomerId(null);

        String validateProjectContent = convertProjectDTOToString(addProjectDTO);
        String contentAsString = performRequestFail(validateProjectContent);
        if (!"The customer id must not be null\n".equals(new JSONObject(contentAsString).get("message").toString())) {
            fail();
        }
    }

    @Test
    void postInvalidProjectDueToStartDate() throws Exception {
        addProjectDTO.setStartDate(null);

        String validateProjectContent = convertProjectDTOToString(addProjectDTO);
        String contentAsString = performRequestFail(validateProjectContent);
        if (!"The start date must not be null\n".equals(new JSONObject(contentAsString).get("message").toString())) {
            fail();
        }
    }

    @Test
    void postInvalidProjectDueToPlannedEndDate() throws Exception {
        addProjectDTO.setPlannedEndDate(null);

        String validateProjectContent = convertProjectDTOToString(addProjectDTO);
        String contentAsString = performRequestFail(validateProjectContent);
        if (!"The planned end date must not be null\n".equals(new JSONObject(contentAsString).get("message").toString())) {
            fail();
        }
    }

    @Test
    void postInvalidProjectDueToProjectLeader() throws Exception {
        addProjectDTO.setProjectLeader(null);

        String validateProjectContent = convertProjectDTOToString(addProjectDTO);
        String contentAsString = performRequestFail(validateProjectContent);
        if (!"The project needs a leader\n".equals(new JSONObject(contentAsString).get("message").toString())) {
            fail();
        }
    }

    @Test
    void postInvalidProjectDueToQualifications() throws Exception {
        addProjectDTO.setQualifications(null);
        addProjectDTO.setAddEmployeeDTOs(null);

        String validateProjectContent = convertProjectDTOToString(addProjectDTO);
        String contentAsString = performRequestFail(validateProjectContent);
        if (!"The list of qualifications must not be null\n".equals(new JSONObject(contentAsString).get("message").toString())) {
            fail();
        }
    }
    
    private String performRequest(String validProjectContent) throws Exception {
        return this.mockMvc.perform(post("/v1/api/pms/project").content(validProjectContent).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("description", is(addProjectDTO.getDescription())))
                .andExpect(jsonPath("customer.id", is(addProjectDTO.getCustomerId().intValue())))
                .andExpect(jsonPath("comment", is(addProjectDTO.getComment())))
                .andExpect(jsonPath("projectLeader.id", is(addProjectDTO.getProjectLeader().intValue())))
                .andExpect(jsonPath("startDate", containsString(addProjectDTO.getStartDate().toString())))
                .andExpect(jsonPath("plannedEndDate", containsString(addProjectDTO.getPlannedEndDate().toString())))
                .andExpect(jsonPath("endDate", is(addProjectDTO.getEndDate())))
                .andExpect(jsonPath("projectEmployees", hasSize(2)))
                .andExpect(jsonPath("projectEmployees.*.id", hasItems(addEmployeeOne.getId().intValue(), addEmployeeTwo.getId().intValue())))
                .andExpect(jsonPath("qualifications", hasSize(2)))
                .andExpect(jsonPath("qualifications.*", hasItems(addProjectDTO.getQualifications().get(0), addProjectDTO.getQualifications().get(1))))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private String performRequestFail(String validProjectContent) throws Exception {
        return this.mockMvc.perform(post("/v1/api/pms/project").content(validProjectContent).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
    
    private String convertProjectDTOToString(AddProjectDTO addProjectDTO) {
        String qualificationsAsString = "null";
        if (addProjectDTO.getQualifications() != null) {
            qualificationsAsString = "[\"" + addProjectDTO.getQualifications().get(0) + "\",\""+ addProjectDTO.getQualifications().get(1) + "\"]";
        }
        String addEmployeeDTOsAsString = "[]";
        if (addProjectDTO.getAddEmployeeDTOs() != null) {
            addEmployeeDTOsAsString = addEmployeeDTOs;
        }
        String startDateAsString = "null";
        if (addProjectDTO.getStartDate() != null) {
            startDateAsString = "\"" + addProjectDTO.getStartDate() + "\"";
        }
        String plannedEndDateAsString = "null";
        if (addProjectDTO.getPlannedEndDate() != null) {
            plannedEndDateAsString = "\"" + addProjectDTO.getPlannedEndDate() + "\"";
        }
        return "{" +
                "\"description\": \"" + addProjectDTO.getDescription() + "\"," +
                "\"customerId\": " + addProjectDTO.getCustomerId() + "," +
                "\"comment\": \"" + addProjectDTO.getComment() +  "\"," +
                "\"projectLeader\": " + addProjectDTO.getProjectLeader() + "," +
                "\"startDate\": " + startDateAsString + "," +
                "\"plannedEndDate\": " + plannedEndDateAsString + "," +
                "\"endDate\": \"\"," +
                "\"addEmployeeDTOs\": " + addEmployeeDTOsAsString + "," +
                "\"qualifications\": " + qualificationsAsString +
                "}";
    }
    
    private void validateLoadedProjectEntity(ProjectEntity loadedProjectEntity) {
        List<Long> loadedProjectEntityEmployeeIds = new ArrayList<>();
        for (EmployeeProjectEntity employeeProjectEntity : loadedProjectEntity.getProjectEmployees()) {
            loadedProjectEntityEmployeeIds.add(employeeProjectEntity.getEmployeeId());
        }
        List<String> loadedProjectQualifications = new ArrayList<>();
        for (ProjectQualificationEntity projectQualification : loadedProjectEntity.getProjectQualifications()) {
            loadedProjectQualifications.add(projectQualification.getQualification());
        }
        assertThat(loadedProjectEntity.getDescription()).isEqualTo(addProjectDTO.getDescription());
        assertThat(loadedProjectEntity.getCustomerId()).isEqualTo(addProjectDTO.getCustomerId());
        assertThat(loadedProjectEntity.getComment()).isEqualTo(addProjectDTO.getComment());
        assertThat(loadedProjectEntity.getProjectLeader()).isEqualTo(addProjectDTO.getProjectLeader());
        assertThat(loadedProjectEntity.getStartDate()).isEqualTo(addProjectDTO.getStartDate());
        assertThat(loadedProjectEntity.getPlannedEndDate()).isEqualTo(addProjectDTO.getPlannedEndDate());
        assertThat(loadedProjectEntity.getEndDate()).isEqualTo(addProjectDTO.getEndDate());
        assertThat(loadedProjectEntityEmployeeIds).hasSameElementsAs(List.of(addEmployeeOne.getId(), addEmployeeTwo.getId()));
        assertThat(loadedProjectQualifications).hasSameElementsAs(addProjectDTO.getQualifications());
    }
}
