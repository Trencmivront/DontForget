package main.java.services.tasktag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.TaskTag;
import main.java.repos.TaskTagRepository;

@Service
public class CreateTaskTagService {

	private static final Logger logger = LoggerFactory.getLogger(CreateTaskTagService.class.getName());

	@Autowired
	private TaskTagRepository taskTagRepository;

	public CreateTaskTagService(TaskTagRepository taskTagRepository) {
		this.taskTagRepository = taskTagRepository;
	}

	public ResponseEntity<String> execute(TaskTag taskTag) {
		logger.info("Executing {} for taskTag: {}", this.getClass(), taskTag);
		try {
			taskTagRepository.save(taskTag);
			logger.info("TaskTag saved successfully.");
			return ResponseEntity.status(HttpStatus.CREATED).body("TASK TAG CREATED");
		} catch (Exception e) {
			logger.error("Database error: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO CREATE TASK TAG");
		}
	}
}
