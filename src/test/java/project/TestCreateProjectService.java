package test.java.project;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.dco.ProjectDCO;
import main.java.repos.ProjectRepository;
import main.java.services.project.CreateProjectService;

@ExtendWith(MockitoExtension.class)
public class TestCreateProjectService {

	@Mock
	private ProjectRepository projectRepository;
	
	@InjectMocks
	private CreateProjectService createProjectService;
	
	private ProjectDCO sampleProject;
	
	@BeforeEach
	void init() {
		sampleProject = new ProjectDCO("Hello", "", 1L);
	}
	
	@Test
	void testServiceReturnsTrueResult() {
		when(createProjectService.execute(sampleProject));
		
		
		
	}
	
	
}
