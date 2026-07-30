package main.java.services.tasktag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.inter.Command;
import main.java.repos.TaskTagRepository;

@Service
public class DeleteTagsOfTaskService implements Command<Long> {

	private static final Logger logger = LoggerFactory.getLogger(DeleteTagsOfTaskService.class.getName());

	@Autowired
	private TaskTagRepository taskTagRepository;

	public DeleteTagsOfTaskService(TaskTagRepository taskTagRepository) {
		this.taskTagRepository = taskTagRepository;
	}

	public ResponseEntity<String> execute(Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		try {
			taskTagRepository.deleteBytaskId(taskId);
			logger.info("All tags for task ID {} deleted successfully.", taskId);
			return ResponseEntity.ok("TAGS OF TASK DELETED");
		} catch (Exception e) {
			logger.warn("Error deleting tags for task ID {}: {}", taskId, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO DELETE TAGS OF TASK");
		}
	}
}
