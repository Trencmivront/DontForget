package main.services.reminder;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.notify.NotificationManager;
import main.repos.ReminderRepository;
import main.services.recurring.DeleteRecurringTaskService;

@Service
public class DeleteReminderService {

	private static final Logger logger = Logger.getLogger(DeleteReminderService.class.getName());

	@Autowired
	private ReminderRepository reminderRepository;

	@Autowired
	private DeleteRecurringTaskService deleteRecurringTaskService;

	public boolean execute(Long id) {
		logger.info("Class " + logger.getName() + " is executed with input id: " + id);
		try {
			// we need to delete recurring task and its connections first
			deleteRecurringTaskService.execute(id);
			reminderRepository.deleteByTask_id(id);
			logger.info("Reminder deleted.");

			// Also delete the reminder from here
			NotificationManager nm = new NotificationManager();
			nm.cancelReminder(id);
			return true;
		} catch (Exception e) {
			logger.warning("Error deleting reminder for ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
