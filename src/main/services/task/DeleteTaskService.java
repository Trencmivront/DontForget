package main.services.task;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.repos.TaskRepository;
import main.services.reminder.DeleteReminderService;
import main.services.tasktag.DeleteTaskTagService;

@Service
public class DeleteTaskService {

	private static final Logger logger = Logger.getLogger(DeleteTaskService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private DeleteReminderService deleteReminderService;

	@Autowired
	private DeleteTaskTagService deleteTaskTagService;

	public boolean execute(Long id) {
		logger.info("Class " + logger.getName() + " is executed with input id: " + id);

		// 1. Delete associated connections using the specialized service classes
		deleteReminderService.execute(id);
		deleteTaskTagService.execute(id);

		// 2. Delete the task record itself
		try {
			if (!taskRepository.existsById(id)) {
				logger.warning("Task not found with ID: " + id);
				return false;
			}
			taskRepository.deleteById(id);
			logger.info("Task deleted.");
			return true;
		} catch (Exception e) {
			logger.warning("Error deleting task with ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
