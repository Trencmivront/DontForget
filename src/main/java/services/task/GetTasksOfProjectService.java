package main.java.services.task;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dto.TaskDTO;
import main.java.entities.Task;
import main.java.repos.TaskRepository;

@Service
public class GetTasksOfProjectService {

	private static final Logger logger = LoggerFactory.getLogger(GetTasksOfProjectService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public GetTasksOfProjectService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public ResponseEntity<List<TaskDTO>> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		try {
			List<Task> tasks = taskRepository.findByprojectId(id);
			List<TaskDTO> dtos = tasks.stream()
				.map(task -> new TaskDTO(
					task.getTaskId(),
					task.getTaskTitle(),
					task.getDescription(),
					task.getStatusId(),
					task.getPriority(),
					task.getDueDate() != null ? task.getDueDate().toLocalDateTime().toLocalDate() : null,
					task.getProjectId()
				))
				.toList();
			return ResponseEntity.ok(dtos);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JDialog(), "Error while getting tasks for project id=" + id);
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
