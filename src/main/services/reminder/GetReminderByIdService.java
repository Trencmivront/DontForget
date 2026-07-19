package main.services.reminder;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Reminder;
import main.repos.ReminderRepository;

@Service
public class GetReminderByIdService {

	private static final Logger logger = Logger.getLogger(GetReminderByIdService.class.getName());

	@Autowired
	private ReminderRepository reminderRepository;

	public Reminder execute(Long id) {
		logger.info("Class " + logger.getName() + " is executed with id: " + id);
		try {
			return reminderRepository.findById(id).orElse(null);
		} catch (Exception e) {
			logger.warning("Error getting reminder for ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
