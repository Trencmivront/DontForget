package main.services.recurring;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.repos.RecurringTaskRepository;

@Service
public class DeleteRecurringTaskService {

	private static final Logger logger = Logger.getLogger(DeleteRecurringTaskService.class.getName());

	@Autowired
	private RecurringTaskRepository recurringTaskRepository;

	public boolean execute(Long id) {
		logger.info(String.format("Class %s is executed with input id: %d", logger.getName(), id));
		try {
			recurringTaskRepository.deleteByTask_id(id);
			logger.info("Recurring task deleted.");
			return true;
		} catch (Exception e) {
			logger.warning("Error deleting recurring task for ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
