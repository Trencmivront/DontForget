package main.java.services.task;

import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.Task;
import main.java.repos.TaskRepository;

@Service
public class UpdateTaskService {

	private static final Logger logger = LoggerFactory.getLogger(UpdateTaskService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public UpdateTaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public ResponseEntity<String> execute(Task task) {
		logger.info("Executing {} for task: {}", this.getClass(), task);

		if (task == null || task.taskId() == null) {
			logger.warn("Task or Task ID is null. Aborting update.");
			return ResponseEntity.badRequest().body("INVALID TASK DATA");
		}

		try {
			Task existing = taskRepository.findById(task.taskId()).orElse(null);
			if (existing == null) {
				logger.warn("Task not found with ID: {}", task.taskId());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TASK NOT FOUND");
			}

			existing.settaskTitle(task.taskTitle());
			existing.setDescription(task.description() == null || task.description().isEmpty() ? null : task.description());
			existing.setstatusId(task.statusId() != null ? task.statusId() : 1L);
			existing.setPriority(task.priority() != 0 ? task.priority() : null);
			existing.setdueDate(task.dueDate());
			existing.setlistOrder(task.listOrder() != 0 ? task.listOrder() : null);
			existing.setprojectId(task.projectId() != null && task.projectId() != 0L ? task.projectId() : null);

			// Handle completedAt timestamp:
			// If status is COMPLETED (2), set completedAt (either provided or current time).
			// Otherwise (ACTIVE or other), reset completedAt to null.
			if (task.statusId() != null && task.statusId() == 2L) {
				existing.setcompletedAt(task.completedAt() != null ? task.completedAt() : new Timestamp(System.currentTimeMillis()));
			} else {
				existing.setcompletedAt(null);
			}

			taskRepository.save(existing);
			logger.info("Task update complete.");
			return ResponseEntity.ok("TASK UPDATED");
		} catch (Exception e) {
			logger.error("Exception occurred: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO UPDATE TASK");
		}
	}
}
