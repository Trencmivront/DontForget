package main.java.services.inbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.Inbox;
import main.java.repos.InboxRepository;

@Service
public class CreateMessageService {

	private static final Logger logger = LoggerFactory.getLogger(CreateMessageService.class.getName());

	@Autowired
	private InboxRepository inboxRepository;

	public CreateMessageService(InboxRepository inboxRepository) {
		this.inboxRepository = inboxRepository;
	}

	public ResponseEntity<String> execute(String message) {
		logger.info("Executing {} for message: {}", this.getClass(), message);
		try {
			Inbox inbox = new Inbox();
			inbox.setMessage(message);
			System.out.println(inbox.getCreatedAt());
			inboxRepository.save(inbox);
			logger.info("Message saved successfully.");
			return ResponseEntity.status(HttpStatus.CREATED).body("MESSAGE CREATED");
		} catch (Exception e) {
			logger.error("Database error: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("FAILED TO CREATE MESSAGE");
		}
	}
}
