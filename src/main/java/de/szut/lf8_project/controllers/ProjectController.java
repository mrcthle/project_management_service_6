package de.szut.lf8_project.controllers;

import de.szut.lf8_project.dtos.projectDto.AddProjectDTO;
import de.szut.lf8_project.dtos.projectDto.GetProjectDTO;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.mappers.ProjectMapper;
import de.szut.lf8_project.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/api/pms/project")
public class ProjectController {
    
    private final ProjectMapper projectMapper;
    private final ProjectService projectService;
    
    public ProjectController(
            ProjectMapper projectMapper,
            ProjectService projectService
    ) {
        this.projectMapper = projectMapper;
        this.projectService = projectService;
    }
    
    @PostMapping()
    public ResponseEntity<GetProjectDTO> createProject(@RequestBody @Valid AddProjectDTO addProjectDTO) {
        ProjectEntity newProjectEntity = projectMapper.mapAddProjectDtoToEntity(addProjectDTO);
        newProjectEntity = projectService.create(newProjectEntity);
        GetProjectDTO responseDto = projectMapper.mapToGetDto(newProjectEntity);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<GetProjectDTO> deleteProjectById(@PathVariable Long id) {
        ProjectEntity projectEntity = projectService.delete(id);
        GetProjectDTO getProjectDTO = projectMapper.mapToGetDto(projectEntity);
        return new ResponseEntity<>(getProjectDTO, HttpStatus.OK);
    }
}
