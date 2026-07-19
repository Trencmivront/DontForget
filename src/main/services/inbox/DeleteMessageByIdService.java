package main.services.inbox;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.repos.InboxRepository;

@Service
public class DeleteMessageByIdService {

	private static final Logger logger = Logger.getLogger(DeleteMessageByIdService.class.getName());

	@Autowired
	private InboxRepository inboxRepository;

	public boolean execute(Long id) {
		logger.info("Class " + logger.getName() + " is executed with input id:" + id);
		try {
			if (!inboxRepository.existsById(id)) {
				logger.warning("Message not found with ID: " + id);
				return false;
			}
			inboxRepository.deleteById(id);
			logger.info("Message deleted.");
			return true;
		} catch (Exception e) {
			logger.warning("Error deleting inbox message with ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
