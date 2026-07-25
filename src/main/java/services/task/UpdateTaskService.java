package main.java.services.task;

import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dto.TaskDTO;
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

	public ResponseEntity<String> execute(TaskDTO task, Long id) {
		logger.info("Executing {} for task DTO: {}, id: {}", this.getClass(), task, id);

		if (task == null || id == null) {
			logger.warn("Task or ID is null. Aborting update.");
			return ResponseEntity.badRequest().body("INVALID TASK DATA");
		}

		try {
			Task existing = taskRepository.findById(id).orElse(null);
			if (existing == null) {
				logger.warn("Task not found with ID: {}", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TASK NOT FOUND");
			}

			existing.setTaskTitle(task.getTaskTitle());
			existing.setDescription(task.getDescription());
			existing.setStatusId(task.getStatusId() != null ? task.getStatusId() : 1L);
			existing.setPriority(task.getPriority());
			existing.setDueDate(task.getDueDate() != null ? Timestamp.valueOf(task.getDueDate().atStartOfDay()) : null);
			existing.setProjectId(task.getProjectId() != null && task.getProjectId() != 0L ? task.getProjectId() : null);

			// Handle completedAt timestamp:
			// If status is COMPLETED (2), set completedAt if not already set.
			// Otherwise (ACTIVE or other), reset completedAt to null.
			if (task.getStatusId() != null && task.getStatusId() == 2L) {
				if (existing.getCompletedAt() == null) {
					existing.setCompletedAt(new Timestamp(System.currentTimeMillis()));
				}
			} else {
				existing.setCompletedAt(null);
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
