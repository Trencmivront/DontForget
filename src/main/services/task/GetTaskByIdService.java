package main.services.task;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Task;
import main.repos.TaskRepository;

@Service
public class GetTaskByIdService {

	private static final Logger logger = Logger.getLogger(GetTaskByIdService.class.getName());

	@Autowired
	private TaskRepository taskRepository;

	public Task execute(Long id) {
		try {
			return taskRepository.findById(id).orElse(null);
		} catch (Exception e) {
			logger.warning("An exception occurred: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
