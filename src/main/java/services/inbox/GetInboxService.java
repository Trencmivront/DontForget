package main.java.services.inbox;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import main.java.entities.Inbox;
import main.java.repos.InboxRepository;

@Service
public class GetInboxService {

	private static final Logger logger = LoggerFactory.getLogger(GetInboxService.class.getName());

	@Autowired
	private InboxRepository inboxRepository;

	public GetInboxService(InboxRepository inboxRepository) {
		this.inboxRepository = inboxRepository;
	}

	public ResponseEntity<List<Inbox>> execute() {
		logger.info("Executing {}", this.getClass());
		try {
			List<Inbox> inboxList = inboxRepository.findAll(Sort.by(Sort.Direction.DESC, "inboxId"));
			return ResponseEntity.ok(inboxList);
		} catch (Exception e) {
			logger.warn("Error getting inbox records: {}", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
