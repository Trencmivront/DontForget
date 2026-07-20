package main.java.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.entities.Reminder;
import main.java.services.reminder.CreateReminderService;
import main.java.services.reminder.DeleteReminderService;
import main.java.services.reminder.GetReminderByIdService;
import main.java.services.reminder.GetRemindersService;

@RestController
@RequestMapping("/api/reminder")
public class ReminderController {

	private static final Logger logger = LoggerFactory.getLogger(ReminderController.class.getName());

	private final CreateReminderService createReminderService;
	private final DeleteReminderService deleteReminderService;
	private final GetReminderByIdService getReminderByIdService;
	private final GetRemindersService getRemindersService;

	public ReminderController(CreateReminderService createReminderService,
			DeleteReminderService deleteReminderService,
			GetReminderByIdService getReminderByIdService,
			GetRemindersService getRemindersService) {
		logger.info("Initializing ReminderController");
		this.createReminderService = createReminderService;
		this.deleteReminderService = deleteReminderService;
		this.getReminderByIdService = getReminderByIdService;
		this.getRemindersService = getRemindersService;
	}

	@PostMapping("/create")
	public ResponseEntity<String> createReminder(@RequestBody Reminder reminder) {
		logger.info("Executing {} for reminder: {}", this.getClass(), reminder);
		return createReminderService.execute(reminder);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteReminder(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return deleteReminderService.execute(id);
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Reminder> getReminderById(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return getReminderByIdService.execute(id);
	}

	@GetMapping("/get-all")
	public ResponseEntity<List<Reminder>> getReminders() {
		logger.info("Executing {}", this.getClass());
		return getRemindersService.execute();
	}
}
