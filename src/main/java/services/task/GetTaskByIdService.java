package main.java.services.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dto.TaskDTO;
import main.java.entities.Task;
import main.java.repos.TaskRepository;

@Service
public class GetTaskByIdService {

	private static final Logger logger = LoggerFactory.getLogger(GetTaskByIdService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public GetTaskByIdService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public ResponseEntity<TaskDTO> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		try {
			Task task = taskRepository.findById(id).orElse(null);
			if (task == null) {
				return ResponseEntity.notFound().build();
			}
			TaskDTO dto = new TaskDTO(
				task.getTaskId(),
				task.getTaskTitle(),
				task.getDescription(),
				task.getStatusId(),
				task.getPriority(),
				task.getDueDate() != null ? task.getDueDate().toLocalDateTime().toLocalDate() : null,
				task.getProjectId()
			);
			return ResponseEntity.ok(dto);
		} catch (Exception e) {
			logger.warn("An exception occurred: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

}
