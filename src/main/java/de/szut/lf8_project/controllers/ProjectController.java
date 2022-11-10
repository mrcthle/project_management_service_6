package de.szut.lf8_project.controllers;

import de.szut.lf8_project.dtos.employeeDto.GetEmployeeDTO;
import de.szut.lf8_project.dtos.projectDto.AddProjectDTO;
import de.szut.lf8_project.dtos.projectDto.GetProjectDTO;
import de.szut.lf8_project.entities.EmployeeProjectEntity;
import de.szut.lf8_project.entities.ProjectEntity;
import de.szut.lf8_project.mappers.EmployeeMapper;
import de.szut.lf8_project.mappers.ProjectMapper;
import de.szut.lf8_project.services.EmployeeService;
import de.szut.lf8_project.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("v1/api/pms/project")
public class ProjectController {

    private final EmployeeMapper employeeMapper;
    private final EmployeeService employeeService;
    private final ProjectMapper projectMapper;
    private final ProjectService projectService;
    
    public ProjectController(
            EmployeeMapper employeeMapper,
            EmployeeService employeeService,
            ProjectMapper projectMapper,
            ProjectService projectService
    ) {
        this.employeeMapper = employeeMapper;
        this.employeeService = employeeService;
        this.projectMapper = projectMapper;
        this.projectService = projectService;
    }
    
    @GetMapping("/read/{id}")
    public ResponseEntity<GetProjectDTO> readProjectById(@PathVariable Long id) {
        ProjectEntity projectEntity = projectService.readById(id);
        GetProjectDTO getProjectDTO = projectMapper.mapToGetDto(projectEntity);
        return new ResponseEntity<>(getProjectDTO, HttpStatus.OK);
    }
    
    @GetMapping("/read")
    public ResponseEntity<List<GetProjectDTO>> readAllProjectEntities() {
        List<ProjectEntity> projectEntities = projectService.readAll();
        List<GetProjectDTO> getProjectDTOs = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntities) {
            getProjectDTOs.add(projectMapper.mapToGetDto(projectEntity));
        }
        return new ResponseEntity<>(getProjectDTOs, HttpStatus.OK);
        //ToDo: check if we can send a message instead of displaying an empty array
    }
    
    @GetMapping("readEmployees/{id}")
    public ResponseEntity<List<GetEmployeeDTO>> readAllEmployeesByProjectId(@PathVariable Long id) {
        ProjectEntity projectEntity = projectService.readById(id);
        List<GetEmployeeDTO> employees = new ArrayList<>();
        for (EmployeeProjectEntity employeeProjectEntity : projectEntity.getProjectEmployees()) {
            GetEmployeeDTO getEmployeeDTO = 
                    employeeMapper.mapEmployeeDTOToGetEmployeeDTO(
                            employeeService.getEmployee(employeeProjectEntity.getEmployeeId()), 
                            projectEntity
                    );
            employees.add(getEmployeeDTO);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    
    @PostMapping()
    public ResponseEntity<GetProjectDTO> createProject(@RequestBody @Valid AddProjectDTO addProjectDTO) {
        ProjectEntity newProjectEntity = projectMapper.mapAddProjectDtoToEntity(addProjectDTO);
        newProjectEntity = projectService.create(newProjectEntity);
        GetProjectDTO responseDto = projectMapper.mapToGetDto(newProjectEntity);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<GetProjectDTO> updateProjectEntity(@PathVariable Long id, @RequestBody @Valid AddProjectDTO newAddProjectDTO) {
        ProjectEntity updatedProjectEntity = projectMapper.mapAddProjectDtoToEntity(newAddProjectDTO);
        updatedProjectEntity.setPid(id);
        updatedProjectEntity = projectService.update(updatedProjectEntity);
        GetProjectDTO returnProjectDTO = projectMapper.mapToGetDto(updatedProjectEntity);
        return new ResponseEntity<>(returnProjectDTO, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<GetProjectDTO> deleteProjectById(@PathVariable Long id) {
        ProjectEntity projectEntity = projectService.delete(id);
        GetProjectDTO getProjectDTO = projectMapper.mapToGetDto(projectEntity);
        return new ResponseEntity<>(getProjectDTO, HttpStatus.OK);
    }
    
    @DeleteMapping("{projectId}/{employeeId}")
    public ResponseEntity<GetProjectDTO> removeEmployeeFromProject(@PathVariable Long projectId, @PathVariable Long employeeId) {
        ProjectEntity projectEntity = projectService.readById(projectId);
        Set<EmployeeProjectEntity> employeeProjectEntities = projectEntity.getProjectEmployees();
        for (EmployeeProjectEntity employeeProjectEntity : employeeProjectEntities) {
            if (Objects.equals(employeeProjectEntity.getEmployeeId(), employeeId)) {
                employeeProjectEntities.remove(employeeProjectEntity);
                break;
            }
        }
        projectEntity.setProjectEmployees(employeeProjectEntities);
        projectEntity = projectService.update(projectEntity);
        GetProjectDTO getProjectDTO = projectMapper.mapToGetDto(projectEntity);
        return new ResponseEntity<>(getProjectDTO, HttpStatus.OK);
    }
}
