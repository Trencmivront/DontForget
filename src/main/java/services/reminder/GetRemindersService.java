package main.java.services.reminder;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.Reminder;
import main.java.repos.ReminderRepository;

@Service
public class GetRemindersService {

	private static final Logger logger = LoggerFactory.getLogger(GetRemindersService.class.getName());

	@Autowired
	private ReminderRepository reminderRepository;

	public GetRemindersService(ReminderRepository reminderRepository) {
		this.reminderRepository = reminderRepository;
	}

	public ResponseEntity<List<Reminder>> execute() {
		logger.info("Executing {}", this.getClass());
		try {
			List<Reminder> reminders = reminderRepository.findAllOrderByRemindAtAsc();
			return ResponseEntity.ok(reminders);
		} catch (Exception e) {
			logger.warn("Error getting reminder records: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
