package main.java.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.services.recurring.CreateRecurringTaskService;
import main.java.services.recurring.DeleteRecurringTaskService;
import main.java.services.recurring.GetRecurringDaysOfTaskService;

@RestController
@RequestMapping("/api/recurring-task")
public class RecurringTaskController {

	private static final Logger logger = LoggerFactory.getLogger(RecurringTaskController.class.getName());

	private final CreateRecurringTaskService createRecurringTaskService;
	private final DeleteRecurringTaskService deleteRecurringTaskService;
	private final GetRecurringDaysOfTaskService getRecurringDaysOfTaskService;

	public RecurringTaskController(CreateRecurringTaskService createRecurringTaskService,
			DeleteRecurringTaskService deleteRecurringTaskService,
			GetRecurringDaysOfTaskService getRecurringDaysOfTaskService) {
		logger.info("Initializing RecurringTaskController");
		this.createRecurringTaskService = createRecurringTaskService;
		this.deleteRecurringTaskService = deleteRecurringTaskService;
		this.getRecurringDaysOfTaskService = getRecurringDaysOfTaskService;
	}

	@PostMapping("/create/{taskId}")
	public ResponseEntity<String> createRecurringTask(@PathVariable Long taskId, @RequestBody List<DayOfWeek> days) {
		logger.info("Executing {} for taskId: {}, days: {}", this.getClass(), taskId, days);
		return createRecurringTaskService.execute(taskId, days);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteRecurringTask(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return deleteRecurringTaskService.execute(id);
	}

	@GetMapping("/task/{taskId}")
	public ResponseEntity<List<DayOfWeek>> getRecurringDaysOfTask(@PathVariable Long taskId) {
		logger.info("Executing {} for taskId: {}", this.getClass(), taskId);
		return getRecurringDaysOfTaskService.execute(taskId);
	}
}
