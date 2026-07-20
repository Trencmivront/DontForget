package main.java.services.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.Task;
import main.java.repos.TaskRepository;

@Service
public class GetTasksService {

	private static final Logger logger = LoggerFactory.getLogger(GetTasksService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public GetTasksService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public ResponseEntity<List<Task>> execute() {
		logger.info("Executing {}", this.getClass());
		try {
			List<Task> tasks = taskRepository.findAll();
			return ResponseEntity.ok(tasks);
		} catch (Exception e) {
			logger.error("Error fetching tasks: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Collections.emptyList());
		}
	}
}
