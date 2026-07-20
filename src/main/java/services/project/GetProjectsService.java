package main.java.services.project;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.Project;
import main.java.repos.ProjectRepository;

@Service
public class GetProjectsService {

	private static final Logger logger = LoggerFactory.getLogger(GetProjectsService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	public GetProjectsService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public ResponseEntity<List<Project>> execute() {
		logger.info("Executing {}", this.getClass());
		try {
			List<Project> projects = projectRepository.findAllByOrderByListOrderAsc();
			return ResponseEntity.ok(projects);
		} catch (Exception e) {
			logger.error("An exception occurred: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
