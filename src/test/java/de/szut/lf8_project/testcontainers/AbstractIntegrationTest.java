package de.szut.lf8_project.testcontainers;

import de.szut.lf8_project.repositories.EmployeeProjectRepository;
import de.szut.lf8_project.repositories.ProjectQualificationRepository;
import de.szut.lf8_project.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("it")
@ContextConfiguration(initializers = PostgresContextInitializer.class)
public class AbstractIntegrationTest {
    
    @Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    protected EmployeeProjectRepository employeeProjectRepository;
    
    @Autowired
    protected ProjectQualificationRepository projectQualificationRepository;
    
    @Autowired
    protected ProjectRepository projectRepository;
    
    @BeforeEach
    void setUp() {
        employeeProjectRepository.deleteAll();
        projectQualificationRepository.deleteAll();
        projectRepository.deleteAll();
    }
}
