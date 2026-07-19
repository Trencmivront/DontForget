package main.services.task;

import java.sql.Timestamp;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Task;
import main.repos.TaskRepository;

@Service
public class UpdateTaskService {

	private static final Logger logger = Logger.getLogger(UpdateTaskService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public boolean execute(Task task) {
		logger.info("Class " + logger.getName() + " is executed with task ID: " + (task != null ? task.task_id() : "null"));

		if (task == null || task.task_id() == null) {
			logger.warning("Task or Task ID is null. Aborting update.");
			return false;
		}

		try {
			Task existing = taskRepository.findById(task.task_id()).orElse(null);
			if (existing == null) {
				logger.warning("Task not found with ID: " + task.task_id());
				return false;
			}

			existing.setTask_title(task.task_title());
			existing.setDescription(task.description() == null || task.description().isEmpty() ? null : task.description());
			existing.setStatus_id(task.status_id() != null ? task.status_id() : 1L);
			existing.setPriority(task.priority() != 0 ? task.priority() : null);
			existing.setDue_date(task.due_date());
			existing.setList_order(task.list_order() != 0 ? task.list_order() : null);
			existing.setProject_id(task.project_id() != null && task.project_id() != 0L ? task.project_id() : null);

			// Handle completed_at timestamp:
			// If status is COMPLETED (2), set completed_at (either provided or current time).
			// Otherwise (ACTIVE or other), reset completed_at to null.
			if (task.status_id() != null && task.status_id() == 2L) {
				existing.setCompleted_at(task.completed_at() != null ? task.completed_at() : new Timestamp(System.currentTimeMillis()));
			} else {
				existing.setCompleted_at(null);
			}

			taskRepository.save(existing);
			logger.info("Task update complete.");
			return true;
		} catch (Exception e) {
			logger.severe("Exception occurred: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
