package main.services.project;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Project;
import main.repos.ProjectRepository;

@Service
public class GetProjectOfTaskService {

	private static final Logger logger = Logger.getLogger(GetProjectOfTaskService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	public Project execute(Long taskId) {
		logger.info("Class " + logger.getName() + " is executed with task ID: " + taskId);
		try {
			return projectRepository.findByTaskId(taskId);
		} catch (Exception e) {
			logger.severe("An exception occurred: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
