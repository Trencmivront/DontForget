package main.java.services.reminder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import main.java.dto.ReminderDTO;
import main.java.inter.Command;
import main.java.entities.Reminder;
import main.java.notify.NotificationManager;
import main.java.repos.ReminderRepository;

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
			NotificationManager nm = new NotificationManager();
			nm.scheduleReminder(reminder);
			return ResponseEntity.ok("REMINDER UPDATED");
		} catch (Exception e) {
			logger.error("Database error: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO UPDATE REMINDER");
		}
	}
}
