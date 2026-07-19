package main.services.inbox;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entities.Inbox;
import main.repos.InboxRepository;

@Service
public class CreateMessageService {

	private static final Logger logger = Logger.getLogger(CreateMessageService.class.getName());

	@Autowired
	private InboxRepository inboxRepository;

	public boolean execute(String message) {
		logger.info("Executing CreateMessageService.");
		try {
			Inbox inbox = new Inbox();
			inbox.setMessage(message);
			inboxRepository.save(inbox);
			logger.info("Message saved successfully.");
			return true;
		} catch (Exception e) {
			logger.severe("Database error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
