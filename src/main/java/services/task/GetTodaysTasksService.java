package main.java.services.task;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.dto.TaskDTO;
import main.java.entities.Task;
import main.java.inter.Query;
import main.java.repos.TaskRepository;

@Service
public class GetTodaysTasksService implements Query<Void, List<TaskDTO>>{
	private static final Logger logger = LoggerFactory.getLogger(GetTodaysTasksService.class.getName());

	@Autowired
	private TaskRepository taskRepository;
	
	public GetTodaysTasksService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public ResponseEntity<List<TaskDTO>> execute(Void i) {
		logger.info("Executing {}", this.getClass());
		try {
			List<Task> tasks = taskRepository.findTodaysTasks();
			List<TaskDTO> dtos = tasks.stream()
				.map(TaskDTO::new)
				.toList();
			return ResponseEntity.ok(dtos);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Collections.emptyList());
		}
	}
}
