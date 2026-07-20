package main.java.services.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.repos.TagRepository;
import main.java.repos.TaskTagRepository;

@Service
public class DeleteTagService {

	private static final Logger logger = LoggerFactory.getLogger(DeleteTagService.class.getName());

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private TaskTagRepository taskTagRepository;

	public DeleteTagService(TagRepository tagRepository, TaskTagRepository taskTagRepository) {
		this.tagRepository = tagRepository;
		this.taskTagRepository = taskTagRepository;
	}

	public ResponseEntity<String> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);

		// 1. Delete references in the junction table TASK_TAG
		try {
			taskTagRepository.deleteBytagId(id);
		} catch (Exception e) {
			logger.warn("Error deleting TASK_TAG references for tag ID {}: {}", id, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO DELETE TASK TAG REFERENCES");
		}

		// 2. Delete the tag itself
		try {
			if (!tagRepository.existsById(id)) {
				logger.warn("Tag not found with ID: {}", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TAG NOT FOUND");
			}
			tagRepository.deleteById(id);
			logger.info("Tag deleted successfully.");
			return ResponseEntity.ok("TAG DELETED");
		} catch (Exception e) {
			logger.warn("Error deleting tag with ID {}: {}", id, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO DELETE TAG");
		}
	}
}
