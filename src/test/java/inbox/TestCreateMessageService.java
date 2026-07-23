package test.java.inbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import main.java.entities.Inbox;
import main.java.repos.InboxRepository;
import main.java.services.inbox.CreateMessageService;

@ExtendWith(MockitoExtension.class)
class TestCreateMessageService {

	@Mock
	private InboxRepository inboxRepository;

	@InjectMocks
	private CreateMessageService createMessageService;

	@Test
	void testServiceReturnsCreatedStatus() {
		// Arrange
		String message = "Test Message";
		Inbox savedInbox = new Inbox();
		savedInbox.setInboxId(1L);
		savedInbox.setMessage(message);

		when(inboxRepository.findById(any(Long.class))).thenReturn(Optional.of(savedInbox));

		// Act
		ResponseEntity<String> response = createMessageService.execute(message);
		
		Inbox inbox = inboxRepository.findById(1L).orElse(null);
		
		// Assertions
		assertNotNull(response);
		assertEquals(savedInbox, inbox);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("MESSAGE CREATED", response.getBody());

		// Verification
		verify(inboxRepository).save(any(Inbox.class));
	}

	@Test
	void testServiceReturnsInternalServerErrorOnException() {
		// Arrange
		String message = "Test Message";
		when(inboxRepository.save(any(Inbox.class))).thenThrow(new RuntimeException("DB error"));

		// Act
		ResponseEntity<String> response = createMessageService.execute(message);

		// Assertions
		assertNotNull(response);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("FAILED TO CREATE MESSAGE", response.getBody());

		// Verification
		verify(inboxRepository).save(any(Inbox.class));
	}
}
