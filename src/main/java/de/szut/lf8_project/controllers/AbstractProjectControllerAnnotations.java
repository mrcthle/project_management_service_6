package de.szut.lf8_project.controllers;

import de.szut.lf8_project.dtos.employeeDto.GetEmployeeDTO;
import de.szut.lf8_project.dtos.projectDto.AddProjectDTO;
import de.szut.lf8_project.dtos.projectDto.GetProjectDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public abstract class AbstractProjectControllerAnnotations {

    @Operation(summary = "returns projects by its id", description = "The endpoint returns a specific project requested by its id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Read a project by id.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDTO.class)
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
                    description = "A project with this id could not be found.",
                    content = @Content
            )
    })
    abstract public ResponseEntity<GetProjectDTO> readProjectById(Long id);

    @Operation(summary = "returns all projects", description = "The endpoint returns all projects in the database.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Read all projects.",
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
            )
    })
    abstract public ResponseEntity<List<GetProjectDTO>> readAllProjectEntities();

    @Operation(summary = "returns employees by project id",
            description = "The endpoint returns all employees that are part of a specific project.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Read all employees by the id of a project.",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetEmployeeDTO.class))
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
                    description = "A project with this id could not be found.",
                    content = @Content
            )
    })
    abstract public ResponseEntity<List<GetEmployeeDTO>> readAllEmployeesByProjectId(Long id);

    @Operation(summary = "creates a project", description = "The endpoint is used to create a new project.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Project created.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDTO.class)
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
            )
    })
    abstract public ResponseEntity<GetProjectDTO> createProject(AddProjectDTO addProjectDTO);

    @Operation(summary = "updates a project", description = "The endpoint is used to update a specific project by its id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Project updated.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDTO.class)
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
                    description = "A project with this id could not be found.",
                    content = @Content
            )
    })
    abstract public ResponseEntity<GetProjectDTO> updateProjectEntity(Long id, AddProjectDTO newAddProjectDTO);

    @Operation(summary = "deletes a project by its id", description = "The endpoint deletes a specific project requested by its id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Project deleted.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDTO.class)
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
                    description = "A project with this id could not be found.",
                    content = @Content
            )
    })
    abstract public ResponseEntity<GetProjectDTO> deleteProjectById(Long id);

    @Operation(summary = "deletes employee from project", description = "The endpoint deletes a specific employee from a specific project by its id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee deleted from project.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectDTO.class)
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
                    description = "The project or employee with the id could not be found.",
                    content = @Content
            )
    })
    abstract public ResponseEntity<GetProjectDTO> removeEmployeeFromProject(Long projectId, Long employeeId);
}
