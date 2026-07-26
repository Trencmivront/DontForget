package main.java.services.recurring;

import java.time.DayOfWeek;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.RecurringTask;
import main.java.repos.RecurringTaskRepository;

@Service
public class UpdateRecurringTaskService {

	private static final Logger logger = LoggerFactory.getLogger(UpdateRecurringTaskService.class.getName());

	@Autowired
	private RecurringTaskRepository recurringTaskRepository;

	@Autowired
	private DeleteRecurringTaskService deleteRecurringTaskService;

	public UpdateRecurringTaskService(RecurringTaskRepository recurringTaskRepository,
			DeleteRecurringTaskService deleteRecurringTaskService) {
		this.recurringTaskRepository = recurringTaskRepository;
		this.deleteRecurringTaskService = deleteRecurringTaskService;
	}

	public ResponseEntity<String> execute(Long taskId, List<DayOfWeek> days) {
		logger.info("Executing {} for taskId: {}, days: {}", this.getClass(), taskId, days);
		try {
			// Remove all existing recurring day entries for this task
			deleteRecurringTaskService.execute(taskId);
			// Re-insert the new set of days
			for (DayOfWeek day : days) {
				RecurringTask rt = new RecurringTask(taskId, (long) day.getValue());
				recurringTaskRepository.save(rt);
			}
			logger.info("Recurring task updated successfully for taskId: {}", taskId);
			return ResponseEntity.ok("RECURRING TASK UPDATED");
		} catch (Exception e) {
			logger.error("Error updating recurring task: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO UPDATE RECURRING TASK");
		}
	}
}
