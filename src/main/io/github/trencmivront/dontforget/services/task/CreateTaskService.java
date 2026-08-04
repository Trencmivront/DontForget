package main.io.github.trencmivront.dontforget.services.task;

import java.sql.Timestamp;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.io.github.trencmivront.dontforget.dto.TaskDTO;
import main.io.github.trencmivront.dontforget.entities.Task;
import main.io.github.trencmivront.dontforget.inter.Post;
import main.io.github.trencmivront.dontforget.repos.TaskRepository;

@Service
public class CreateTaskService implements Post<TaskDTO> {

	private static final Logger logger = LoggerFactory.getLogger(CreateTaskService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public CreateTaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public ResponseEntity<Long> execute(TaskDTO task) {
		logger.info("Executing {} for task: {}", this.getClass(), task);
		try {
			Long projectId = task.getProjectId();

			// Compute listOrder as MAX + 1 for this project
			int listOrder = taskRepository.findMaxListOrderByProjectId(projectId) + 1;

			Task t = new Task();
			t.setTaskTitle(task.getTaskTitle());
			t.setDescription(task.getDescription() == null || task.getDescription().isEmpty() ? null : task.getDescription());
			t.setStatusId(Objects.requireNonNullElse(task.getStatusId(), 1L)); // 1 = ACTIVE
			t.setPriority(task.getPriority());
			t.setDueDate(task.getDueDate() != null ? Timestamp.valueOf(task.getDueDate().atStartOfDay()) : null);
			t.setListOrder(listOrder);
			t.setProjectId(projectId);

			Task saved = taskRepository.save(t);
			logger.info("Task saved successfully.");
			return ResponseEntity.status(HttpStatus.CREATED).body(saved.getTaskId());
		} catch (Exception e) {
			logger.error("Database error: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(null);
		}
	}
}
