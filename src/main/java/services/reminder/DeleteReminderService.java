package main.java.services.reminder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.notify.NotificationManager;
import main.java.repos.ReminderRepository;
import main.java.services.recurring.DeleteRecurringTaskService;

@Service
public class DeleteReminderService {

	private static final Logger logger = LoggerFactory.getLogger(DeleteReminderService.class.getName());

	@Autowired
	private ReminderRepository reminderRepository;

	@Autowired
	private DeleteRecurringTaskService deleteRecurringTaskService;

	public DeleteReminderService(ReminderRepository reminderRepository, DeleteRecurringTaskService deleteRecurringTaskService) {
		this.reminderRepository = reminderRepository;
		this.deleteRecurringTaskService = deleteRecurringTaskService;
	}

	public ResponseEntity<String> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		try {
			// we need to delete recurring task and its connections first
			deleteRecurringTaskService.execute(id);
			reminderRepository.deleteBytaskId(id);
			logger.info("Reminder deleted.");

			// Also delete the reminder from here
			NotificationManager nm = new NotificationManager();
			nm.cancelReminder(id);
			return ResponseEntity.ok("REMINDER DELETED");
		} catch (Exception e) {
			logger.warn("Error deleting reminder for ID {}: {}", id, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO DELETE REMINDER");
		}
	}
}
