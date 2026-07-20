package main.java.services.inbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.repos.InboxRepository;

@Service
public class DeleteMessageByIdService {

	private static final Logger logger = LoggerFactory.getLogger(DeleteMessageByIdService.class.getName());

	@Autowired
	private InboxRepository inboxRepository;

	public DeleteMessageByIdService(InboxRepository inboxRepository) {
		this.inboxRepository = inboxRepository;
	}

	public ResponseEntity<String> execute(Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		try {
			if (!inboxRepository.existsById(id)) {
				logger.warn("Message not found with ID: {}", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("MESSAGE NOT FOUND");
			}
			inboxRepository.deleteById(id);
			logger.info("Message deleted.");
			return ResponseEntity.ok("MESSAGE DELETED");
		} catch (Exception e) {
			logger.warn("Error deleting inbox message with ID {}: {}", id, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO DELETE MESSAGE");
		}
	}
}
