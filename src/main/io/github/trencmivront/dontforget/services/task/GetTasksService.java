package main.io.github.trencmivront.dontforget.services.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.io.github.trencmivront.dontforget.dto.TaskDTO;
import main.io.github.trencmivront.dontforget.entities.Task;
import main.io.github.trencmivront.dontforget.inter.Query;
import main.io.github.trencmivront.dontforget.repos.TaskRepository;

@Service
public class GetTasksService implements Query<Void, List<TaskDTO>>{

	private static final Logger logger = LoggerFactory.getLogger(GetTasksService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public GetTasksService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public ResponseEntity<List<TaskDTO>> execute(Void i) {
		logger.info("Executing {}", this.getClass());
		try {
			List<Task> tasks = taskRepository.findAll();
			List<TaskDTO> dtos = tasks.stream()
				.map(TaskDTO::new)
				.toList();
			return ResponseEntity.ok(dtos);
		} catch (Exception e) {
			logger.error("Error fetching tasks: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Collections.emptyList());
		}
	}
}
