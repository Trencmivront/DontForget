package main.io.github.trencmivront.dontforget.services.reminder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.io.github.trencmivront.dontforget.dto.ReminderDTO;
import main.io.github.trencmivront.dontforget.entities.Reminder;
import main.io.github.trencmivront.dontforget.inter.Command;
import main.io.github.trencmivront.dontforget.notify.NotificationManager;
import main.io.github.trencmivront.dontforget.repos.ReminderRepository;

import java.sql.Timestamp;

@Service
public class UpdateReminderService implements Command<ReminderDTO> {

	private static final Logger logger = LoggerFactory.getLogger(UpdateReminderService.class.getName());

	@Autowired
	private ReminderRepository reminderRepository;

	public UpdateReminderService(ReminderRepository reminderRepository) {
		this.reminderRepository = reminderRepository;
	}

	public ResponseEntity<String> execute(ReminderDTO reminder) {
		logger.info("Executing {} for reminder: {}", this.getClass(), reminder);
		try {
			Reminder entity = reminderRepository.findById(reminder.getTaskId()).orElse(null);
			if (entity == null) {
				logger.warn("Reminder not found for taskId: {}", reminder.getTaskId());
				return ResponseEntity.notFound().build();
			}
			entity.setRemindAt(reminder.getRemindAt() != null ? Timestamp.valueOf(reminder.getRemindAt()) : null);
			entity.setMessage(reminder.getMessage());
			reminderRepository.save(entity);
			logger.info("Reminder updated successfully for taskId: {}", reminder.getTaskId());
			// Reschedule the notification with the updated time
			NotificationManager.getInstance().scheduleReminder(reminder);
			return ResponseEntity.ok("REMINDER UPDATED");
		} catch (Exception e) {
			logger.error("Database error: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO UPDATE REMINDER");
		}
	}
}
