package main.java.services.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dco.ProjectDCO;
import main.java.entities.Project;
import main.java.repos.ProjectRepository;

@Service
public class CreateProjectService {

	private static final Logger logger = LoggerFactory.getLogger(CreateProjectService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;
	
	public CreateProjectService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public ResponseEntity<Long> execute(ProjectDCO p) {
		logger.info("Executing {} for p: {}", this.getClass(), p);
		try {
			// Compute listOrder as MAX + 1
			int listOrder = projectRepository.findMaxListOrder() + 1;

			Project project = new Project();
			project.setprojectTitle(p.projectTitle());
			project.setDescription(p.description());
			project.setlistOrder(listOrder);
			project.seticonColorId(p.iconColorId());

			Project createdProject = projectRepository.save(project);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdProject.getprojectId());
		} catch (Exception e) {
			logger.warn("{} thrown.", e.getClass().getName());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(0L);
		}
	}

}
