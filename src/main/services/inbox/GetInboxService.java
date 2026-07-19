package main.services.inbox;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import main.entities.Inbox;
import main.repos.InboxRepository;

@Service
public class GetInboxService {

	private static final Logger logger = Logger.getLogger(GetInboxService.class.getName());

	@Autowired
	private InboxRepository inboxRepository;

	public List<Inbox> execute() {
		logger.info("Class " + logger.getName() + " is executed.");
		try {
			return inboxRepository.findAll(Sort.by(Sort.Direction.DESC, "inbox_id"));
		} catch (Exception e) {
			logger.warning("Error getting inbox records: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
