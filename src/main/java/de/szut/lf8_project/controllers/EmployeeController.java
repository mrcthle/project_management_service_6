package de.szut.lf8_project.controllers;

import de.szut.lf8_project.dtos.employeeDto.EmployeeDTO;
import de.szut.lf8_project.dtos.projectDto.GetProjectDTO;
import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.mappers.ProjectMapper;
import de.szut.lf8_project.services.EmployeeService;
import de.szut.lf8_project.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("v1/api/pms/employee")
public class EmployeeController {
    
    private final EmployeeService employeeService;
    private final ProjectMapper projectMapper;
    private final ProjectService projectService;
    
    public EmployeeController(EmployeeService employeeService, ProjectMapper projectMapper, ProjectService projectService) {
        this.employeeService = employeeService;
        this.projectMapper = projectMapper;
        this.projectService = projectService;
    }
    @Operation(summary = "returns projects by employee id", description = "The endpoint returns a list of all projects an employee is part of.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Read all projects by employee id.",
                    content = {@Content(
                                mediaType = "application/json", 
                                array = @ArraySchema(schema = @Schema(implementation = GetProjectDTO.class))
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "The validation of one or more fields failed.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "You are not authorized.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "An employee with this id could not be found.",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<GetProjectDTO>> readAllProjectsByEmployeeId(
            @Parameter(description = "id of employee")
            @PathVariable Long id
    ) {
        EmployeeDTO employee = employeeService.getEmployee(id);
        List<ProjectEntity> projectEntities = projectService.readAll();
        List<GetProjectDTO> projectDTOs = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntities) {
            for (EmployeeProjectEntity employeeProjectEntity : projectEntity.getProjectEmployees()) {
                if (Objects.equals(employeeProjectEntity.getEmployeeId(), employee.getId())) {
                    projectDTOs.add(projectMapper.mapToGetDto(projectEntity));
                    break;
                }
            }
        }
        return new ResponseEntity<>(projectDTOs, HttpStatus.OK);
    }
}
