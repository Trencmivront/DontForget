package main.services.tasktag;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.repos.TaskTagRepository;

@Service
public class DeleteTaskTagService {

	private static final Logger logger = Logger.getLogger(DeleteTaskTagService.class.getName());

	@Autowired
	private TaskTagRepository taskTagRepository;

	public boolean execute(Long id) {
		logger.info("Class " + logger.getName() + " is executed with input id: " + id);
		try {
			taskTagRepository.deleteByTask_id(id);
			logger.info("Task tags deleted.");
			return true;
		} catch (Exception e) {
			logger.warning("Error deleting task tags for ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
