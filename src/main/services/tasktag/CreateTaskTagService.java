package main.services.tasktag;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.TaskTag;
import main.repos.TaskTagRepository;

@Service
public class CreateTaskTagService {

	private static final Logger logger = Logger.getLogger(CreateTaskTagService.class.getName());

	@Autowired
	private TaskTagRepository taskTagRepository;

	public boolean execute(TaskTag taskTag) {
		logger.info("Executing CreateTaskTagService.");
		try {
			taskTagRepository.save(taskTag);
			logger.info("TaskTag saved successfully.");
			return true;
		} catch (Exception e) {
			logger.severe("Database error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
