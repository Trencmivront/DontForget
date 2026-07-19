package main.services.tasktag;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.TaskTag;
import main.repos.TaskTagRepository;

@Service
public class GetTaskTagByTaskService {

	private static final Logger logger = Logger.getLogger(GetTaskTagByTaskService.class.getName());

	@Autowired
	private TaskTagRepository taskTagRepository;

	public List<TaskTag> execute(Long taskId) {
		logger.info("Executing GetTaskTagByTaskService with taskId: " + taskId);
		try {
			return taskTagRepository.findByTask_id(taskId);
		} catch (Exception e) {
			logger.warning("Error fetching task tags for task ID " + taskId + ": " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
