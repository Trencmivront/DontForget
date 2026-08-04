package main.io.github.trencmivront.dontforget.services.reminder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.io.github.trencmivront.dontforget.dto.ReminderDTO;
import main.io.github.trencmivront.dontforget.entities.Reminder;
import main.io.github.trencmivront.dontforget.inter.Query;
import main.io.github.trencmivront.dontforget.repos.ReminderRepository;

@Service
public class GetReminderByIdService implements Query<Long, ReminderDTO>{

	private static final Logger logger = LoggerFactory.getLogger(GetReminderByIdService.class.getName());

	@Autowired
	private ReminderRepository reminderRepository;

	public GetReminderByIdService(ReminderRepository reminderRepository) {
		this.reminderRepository = reminderRepository;
	}

	public ResponseEntity<ReminderDTO> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		try {
			Reminder reminder = reminderRepository.findById(id).orElse(null);
			if (reminder == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(new ReminderDTO(reminder));
		} catch (Exception e) {
			logger.warn("Error getting reminder for ID {}: {}", id, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
