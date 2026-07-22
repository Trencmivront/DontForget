package test.java.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import main.java.dco.ProjectDCO;
import main.java.entities.Project;
import main.java.repos.ProjectRepository;
import main.java.services.project.CreateProjectService;

@ExtendWith(MockitoExtension.class)
public class TestCreateProjectService {

	@Mock
	private ProjectRepository projectRepository;
	
	@InjectMocks
	private CreateProjectService createProjectService;
	
	private ProjectDCO sampleProjectDCO;
	private Project savedProject;
	
	@BeforeEach
	void init() {
		sampleProjectDCO = new ProjectDCO("Hello", "Description", 1L);
		
		savedProject = new Project();
		savedProject.setprojectId(123L);
		savedProject.setprojectTitle("Hello");
		savedProject.setDescription("Description");
		savedProject.setlistOrder(6);
		savedProject.seticonColorId(1L);
	}
	
	@Test
	void testServiceReturnsTrueResult() {
		// Mock behavior
		when(projectRepository.findMaxListOrder()).thenReturn(5);
		when(projectRepository.save(any(Project.class))).thenReturn(savedProject);
		
		// Execute service
		ResponseEntity<Long> response = createProjectService.execute(sampleProjectDCO);
		
		// Assertions
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(123L, response.getBody());
		
		// Verifications
		verify(projectRepository).findMaxListOrder();
		verify(projectRepository).save(any(Project.class));
	}
	
	@Test
	void testServiceReturnsNullValueOnException() {
		// Mock exception
		when(projectRepository.findMaxListOrder()).thenThrow(new RuntimeException("DB error"));
		
		// Execute service
		ResponseEntity<Long> response = createProjectService.execute(sampleProjectDCO);
		
		// Assertions
		assertNotNull(response);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals(0L, response.getBody());
		
		// Verifications
		verify(projectRepository).findMaxListOrder();
	}
}
