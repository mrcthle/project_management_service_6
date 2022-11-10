package de.szut.lf8_project.dtos.employeeDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record AddEmployeeDTO(@NotNull Long id, @NotBlank String skillWithinProject) {
}
