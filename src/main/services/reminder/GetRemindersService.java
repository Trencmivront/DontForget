package main.services.reminder;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Reminder;
import main.repos.ReminderRepository;

@Service
public class GetRemindersService {

	private static final Logger logger = Logger.getLogger(GetRemindersService.class.getName());

	@Autowired
	private ReminderRepository reminderRepository;

	public List<Reminder> execute() {
		logger.info("Class " + logger.getName() + " is executed.");
		try {
			return reminderRepository.findAllOrderByRemindAtAsc();
		} catch (Exception e) {
			logger.warning("Error getting reminder records: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
