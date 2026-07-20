package main.java.services.reminder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.Reminder;
import main.java.repos.ReminderRepository;

@Service
public class GetReminderByIdService {

	private static final Logger logger = LoggerFactory.getLogger(GetReminderByIdService.class.getName());

	@Autowired
	private ReminderRepository reminderRepository;

	public GetReminderByIdService(ReminderRepository reminderRepository) {
		this.reminderRepository = reminderRepository;
	}

	public ResponseEntity<Reminder> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		try {
			Reminder reminder = reminderRepository.findById(id).orElse(null);
			if (reminder == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(reminder);
		} catch (Exception e) {
			logger.warn("Error getting reminder for ID {}: {}", id, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
