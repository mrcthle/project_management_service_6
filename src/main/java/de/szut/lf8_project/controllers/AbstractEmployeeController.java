package de.szut.lf8_project.controllers;

import de.szut.lf8_project.dtos.projectDto.GetProjectDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public abstract class AbstractEmployeeController {

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
    abstract public ResponseEntity<List<GetProjectDTO>> readAllProjectsByEmployeeId(
            Long id
    );
}
