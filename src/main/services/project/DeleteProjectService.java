package main.services.project;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Task;
import main.repos.ProjectRepository;
import main.repos.TaskRepository;
import main.services.task.DeleteTaskService;

@Service
public class DeleteProjectService {

	private static final Logger logger = Logger.getLogger(DeleteProjectService.class.getName());

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private DeleteTaskService deleteTaskService;

	public boolean execute(Long id) {
		logger.info("Class " + logger.getName() + " is executed with input id: " + id);

		// 1. Fetch all task_ids of this project
		List<Task> tasks = taskRepository.findByProject_id(id);

		// 2. Delete each task using DeleteTaskService
		for (Task task : tasks) {
			boolean taskDeleted = deleteTaskService.execute(task.task_id());
			if (!taskDeleted) {
				logger.warning("Failed to delete task with ID: " + task.task_id());
			}
		}

		// 3. Delete the project itself
		try {
			if (!projectRepository.existsById(id)) {
				logger.warning("Project not found with ID: " + id);
				return false;
			}
			projectRepository.deleteById(id);
			logger.info("Project deleted successfully.");
			return true;
		} catch (Exception e) {
			logger.warning("Error deleting project with ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
