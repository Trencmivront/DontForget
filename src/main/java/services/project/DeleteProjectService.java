package main.java.services.project;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.Task;
import main.java.repos.ProjectRepository;
import main.java.repos.TaskRepository;
import main.java.services.task.DeleteTaskService;

@Service
public class DeleteProjectService {

	private static final Logger logger = LoggerFactory.getLogger(DeleteProjectService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private DeleteTaskService deleteTaskService;

	public DeleteProjectService(ProjectRepository projectRepository, TaskRepository taskRepository, DeleteTaskService deleteTaskService) {
		this.projectRepository = projectRepository;
		this.taskRepository = taskRepository;
		this.deleteTaskService = deleteTaskService;
	}

	public ResponseEntity<String> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);

		// 1. Fetch all taskIds of this project
		List<Task> tasks = taskRepository.findByprojectId(id);

		// 2. Delete each task using DeleteTaskService
		for (Task task : tasks) {
			ResponseEntity<String> res = deleteTaskService.execute(task.taskId());
			if (res.getStatusCode().isError()) {
				logger.warn("Failed to delete task with ID: {}", task.taskId());
			}
		}

		// 3. Delete the project itself
		try {
			if (!projectRepository.existsById(id)) {
				logger.warn("Project not found with ID: {}", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PROJECT NOT FOUND");
			}
			projectRepository.deleteById(id);
			logger.info("Project deleted successfully.");
			return ResponseEntity.ok("PROJECT DELETED");
		} catch (Exception e) {
			logger.warn("Error deleting project with ID {}: {}", id, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO DELETE PROJECT");
		}
	}
}
