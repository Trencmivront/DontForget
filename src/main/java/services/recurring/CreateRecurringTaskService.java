package main.java.services.recurring;

import java.time.DayOfWeek;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.RecurringTask;
import main.java.repos.RecurringTaskRepository;

@Service
public class CreateRecurringTaskService {

	private static final Logger logger = LoggerFactory.getLogger(CreateRecurringTaskService.class.getName());

	@Autowired
	private RecurringTaskRepository recurringTaskRepository;

	public CreateRecurringTaskService(RecurringTaskRepository recurringTaskRepository) {
		this.recurringTaskRepository = recurringTaskRepository;
	}

	public ResponseEntity<String> execute(Long taskId, List<DayOfWeek> days) {
		logger.info("Executing {} for taskId: {}, days: {}", this.getClass(), taskId, days);
		try {
			for (DayOfWeek day : days) {
				RecurringTask rt = new RecurringTask(taskId, (long) day.getValue());
				recurringTaskRepository.save(rt);
			}
			logger.info("Recurring task created successfully for task: {}", taskId);
			return ResponseEntity.status(HttpStatus.CREATED).body("RECURRING TASK CREATED");
		} catch (Exception e) {
			logger.error("Error creating recurring task: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO CREATE RECURRING TASK");
		}
	}
}
