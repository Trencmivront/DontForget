package main.services.task;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Task;
import main.repos.TaskRepository;

@Service
public class GetTodaysTasksService {

	private static final Logger logger = Logger.getLogger(GetTodaysTasksService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public List<Task> execute() {
		logger.info("Service executed.");
		try {
			return taskRepository.findTodaysTasks();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
}
