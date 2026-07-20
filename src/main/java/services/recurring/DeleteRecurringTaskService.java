package main.java.services.recurring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.repos.RecurringTaskRepository;

@Service
public class DeleteRecurringTaskService {

	private static final Logger logger = LoggerFactory.getLogger(DeleteRecurringTaskService.class.getName());

	@Autowired
	private RecurringTaskRepository recurringTaskRepository;

	public DeleteRecurringTaskService(RecurringTaskRepository recurringTaskRepository) {
		this.recurringTaskRepository = recurringTaskRepository;
	}

	public ResponseEntity<String> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		try {
			recurringTaskRepository.deleteBytaskId(id);
			logger.info("Recurring task deleted.");
			return ResponseEntity.ok("RECURRING TASK DELETED");
		} catch (Exception e) {
			logger.warn("Error deleting recurring task for ID {}: {}", id, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO DELETE RECURRING TASK");
		}
	}
}
