package main.java.services.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.Project;
import main.java.repos.ProjectRepository;

@Service
public class GetProjectOfTaskService {

	private static final Logger logger = LoggerFactory.getLogger(GetProjectOfTaskService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	public GetProjectOfTaskService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public ResponseEntity<Project> execute(Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		try {
			Project project = projectRepository.findByTaskId(taskId);
			if (project == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(project);
		} catch (Exception e) {
			logger.error("An exception occurred: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
