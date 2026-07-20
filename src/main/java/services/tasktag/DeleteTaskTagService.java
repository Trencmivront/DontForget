package main.java.services.tasktag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.repos.TaskTagRepository;

@Service
public class DeleteTaskTagService {

	private static final Logger logger = LoggerFactory.getLogger(DeleteTaskTagService.class.getName());

	@Autowired
	private TaskTagRepository taskTagRepository;

	public DeleteTaskTagService(TaskTagRepository taskTagRepository) {
		this.taskTagRepository = taskTagRepository;
	}

	public ResponseEntity<String> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		try {
			taskTagRepository.deleteBytaskId(id);
			logger.info("Task tags deleted.");
			return ResponseEntity.ok("TASK TAG DELETED");
		} catch (Exception e) {
			logger.warn("Error deleting task tags for ID {}: {}", id, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO DELETE TASK TAG");
		}
	}
}
