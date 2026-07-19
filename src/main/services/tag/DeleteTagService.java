package main.services.tag;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.repos.TagRepository;
import main.repos.TaskTagRepository;

@Service
public class DeleteTagService {

	private static final Logger logger = Logger.getLogger(DeleteTagService.class.getName());

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private TaskTagRepository taskTagRepository;

	public boolean execute(Long id) {
		logger.info("Class " + logger.getName() + " is executed with input id: " + id);

		// 1. Delete references in the junction table TASK_TAG
		try {
			taskTagRepository.deleteByTag_id(id);
		} catch (Exception e) {
			logger.warning("Error deleting TASK_TAG references for tag ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}

		// 2. Delete the tag itself
		try {
			if (!tagRepository.existsById(id)) {
				logger.warning("Tag not found with ID: " + id);
				return false;
			}
			tagRepository.deleteById(id);
			logger.info("Tag deleted successfully.");
			return true;
		} catch (Exception e) {
			logger.warning("Error deleting tag with ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
