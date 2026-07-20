package test.java.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import main.java.entities.Project;
import main.java.repos.ProjectRepository;
import main.java.services.project.GetProjectsService;

@ExtendWith(MockitoExtension.class)
class TestGetProjectsService {

	@Mock
	private ProjectRepository projectRepository;
	
	@InjectMocks
	private GetProjectsService getProjectsService;
	
	private Project sampleProject;
	
	@BeforeEach
	void init() {
		sampleProject = new Project();
		sampleProject.setprojectId(1L);
		sampleProject.setprojectTitle("Hello");
	}
	
	@Test
	void testServiceReturnsTrueValue() {
//		test case
		when(projectRepository.findAllByOrderByListOrderAsc()).thenReturn(List.of(sampleProject));
//		values to be tested
		ResponseEntity<List<Project>> response = getProjectsService.execute();
		List<Project> projects = response.getBody();
//		testing
		assertNotNull(projects);
		assertEquals(1, projects.size());
		assertEquals("Hello" , projects.get(0).getprojectTitle());
		
		verify(projectRepository).findAllByOrderByListOrderAsc();
	}
	
	@Test
	void testServiceReturnsNullValueOnException() {
		when(projectRepository.findAllByOrderByListOrderAsc()).thenThrow(new RuntimeException());
		
		ResponseEntity<List<Project>> response = getProjectsService.execute();
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		verify(projectRepository).findAllByOrderByListOrderAsc();
	}
}
