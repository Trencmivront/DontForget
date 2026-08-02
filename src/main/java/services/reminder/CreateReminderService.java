package main.java.services.reminder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import main.java.dto.ReminderDTO;
import main.java.entities.Reminder;
import main.java.notify.NotificationManager;
import main.java.repos.ReminderRepository;

@Service
public class CreateReminderService {

	private static final Logger logger = LoggerFactory.getLogger(CreateReminderService.class.getName());

	@Autowired
	private ReminderRepository reminderRepository;

	public CreateReminderService(ReminderRepository reminderRepository) {
		this.reminderRepository = reminderRepository;
	}

	public ResponseEntity<String> execute(ReminderDTO reminder) {
		logger.info("Executing {} for reminder: {}", this.getClass(), reminder);
		try {
			Reminder entity = new Reminder(
				reminder.getTaskId(),
				reminder.getRemindAt() != null ? Timestamp.valueOf(reminder.getRemindAt()) : null,
				reminder.getMessage()
			);
			reminderRepository.save(entity);
			logger.info("Reminder saved successfully.");
			// Start the reminder once it is saved
			NotificationManager.getInstance().scheduleReminder(reminder);
			return ResponseEntity.status(HttpStatus.CREATED).body("REMINDER CREATED");
		} catch (Exception e) {
			logger.error("Database error: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO CREATE REMINDER");
		}
	}
}
