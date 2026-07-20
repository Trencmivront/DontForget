package main.java.services.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.repos.TaskRepository;
import main.java.services.reminder.DeleteReminderService;
import main.java.services.tasktag.DeleteTaskTagService;

@Service
public class DeleteTaskService {

	private static final Logger logger = LoggerFactory.getLogger(DeleteTaskService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private DeleteReminderService deleteReminderService;

	@Autowired
	private DeleteTaskTagService deleteTaskTagService;

	public DeleteTaskService(TaskRepository taskRepository, DeleteReminderService deleteReminderService, DeleteTaskTagService deleteTaskTagService) {
		this.taskRepository = taskRepository;
		this.deleteReminderService = deleteReminderService;
		this.deleteTaskTagService = deleteTaskTagService;
	}

	public ResponseEntity<String> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);

		// 1. Delete associated connections using the specialized service classes
		deleteReminderService.execute(id);
		deleteTaskTagService.execute(id);

		// 2. Delete the task record itself
		try {
			if (!taskRepository.existsById(id)) {
				logger.warn("Task not found with ID: {}", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TASK NOT FOUND");
			}
			taskRepository.deleteById(id);
			logger.info("Task deleted.");
			return ResponseEntity.ok("TASK DELETED");
		} catch (Exception e) {
			logger.warn("Error deleting task with ID {}: {}", id, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO DELETE TASK");
		}
	}
}
