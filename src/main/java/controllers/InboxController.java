package main.java.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.entities.Inbox;
import main.java.services.inbox.CreateMessageService;
import main.java.services.inbox.DeleteMessageByIdService;
import main.java.services.inbox.GetInboxService;

@RestController
@RequestMapping("/api/inbox")
public class InboxController {

	private static final Logger logger = LoggerFactory.getLogger(InboxController.class.getName());

	private final CreateMessageService createMessageService;
	private final DeleteMessageByIdService deleteMessageByIdService;
	private final GetInboxService getInboxService;

	public InboxController(CreateMessageService createMessageService,
			DeleteMessageByIdService deleteMessageByIdService,
			GetInboxService getInboxService) {
		logger.info("Initializing InboxController");
		this.createMessageService = createMessageService;
		this.deleteMessageByIdService = deleteMessageByIdService;
		this.getInboxService = getInboxService;
	}

	@PostMapping("/create")
	public ResponseEntity<String> createMessage(@RequestBody String message) {
		logger.info("Executing {} for message: {}", this.getClass(), message);
		return createMessageService.execute(message);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteMessageById(@PathVariable Long id) {
		logger.info("Executing {} for id: {}", this.getClass(), id);
		return deleteMessageByIdService.execute(id);
	}

	@GetMapping("/get-all")
	public ResponseEntity<List<Inbox>> getInbox() {
		logger.info("Executing {}", this.getClass());
		return getInboxService.execute();
	}
}
