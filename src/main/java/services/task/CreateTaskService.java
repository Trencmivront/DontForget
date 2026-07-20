package main.java.services.task;

import java.sql.Timestamp;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dco.TaskDCO;
import main.java.entities.Task;
import main.java.repos.TaskRepository;

@Service
public class CreateTaskService {

	private static final Logger logger = LoggerFactory.getLogger(CreateTaskService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public CreateTaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public ResponseEntity<Long> execute(TaskDCO task) {
		logger.info("Executing {} for task: {}", this.getClass(), task);
		try {
			Long projectId = task.projectId();

			// Compute listOrder as MAX + 1 for this project
			int listOrder = taskRepository.findMaxListOrderByProjectId(projectId) + 1;

			Task t = new Task();
			t.settaskTitle(task.taskTitle());
			t.setDescription(task.description() == null || task.description().isEmpty() ? null : task.description());
			t.setstatusId(Objects.requireNonNullElse(task.statusId(), 1L)); // 1 = ACTIVE
			t.setPriority(task.priority());
			t.setdueDate(task.dueDate() != null ? Timestamp.valueOf(task.dueDate().atStartOfDay()) : null);
			t.setlistOrder(listOrder);
			t.setprojectId(projectId);

			Task saved = taskRepository.save(t);
			logger.info("Task saved successfully.");
			return ResponseEntity.status(HttpStatus.CREATED).body(saved.taskId());
		} catch (Exception e) {
			logger.error("Database error: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(0L);
		}
	}
}
