package main.services.reminder;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Reminder;
import main.notify.NotificationManager;
import main.repos.ReminderRepository;

@Service
public class CreateReminderService {

	private static final Logger logger = Logger.getLogger(CreateReminderService.class.getName());

	@Autowired
	private ReminderRepository reminderRepository;

	public boolean execute(Reminder reminder) {
		logger.info("Executing CreateReminderService.");
		try {
			reminderRepository.save(reminder);
			logger.info("Reminder saved successfully.");
			// Start the reminder once it is saved
			NotificationManager nm = new NotificationManager();
			nm.scheduleReminder(reminder);
			return true;
		} catch (Exception e) {
			logger.severe("Database error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
