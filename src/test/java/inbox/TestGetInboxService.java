package test.java.inbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import main.java.entities.Inbox;
import main.java.repos.InboxRepository;
import main.java.services.inbox.GetInboxService;

@ExtendWith(MockitoExtension.class)
class TestGetInboxService {

	@Mock
	private InboxRepository inboxRepository;

	@InjectMocks
	private GetInboxService getInboxService;

	@Test
	void testServiceReturnsInboxListSuccessfully() {
		// Arrange
		Inbox message1 = new Inbox(1L, "First Message", new Timestamp(System.currentTimeMillis()));
		Inbox message2 = new Inbox(2L, "Second Message", new Timestamp(System.currentTimeMillis()));
		List<Inbox> expectedList = List.of(message2, message1);

		when(inboxRepository.findAll(any(Sort.class))).thenReturn(expectedList);

		// Act
		ResponseEntity<List<Inbox>> response = getInboxService.execute();

		// Assertions
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(2, response.getBody().size());
		assertEquals("Second Message", response.getBody().get(0).getMessage());

		// Verification
		verify(inboxRepository).findAll(any(Sort.class));
	}

	@Test
	void testServiceReturnsInternalServerErrorOnException() {
		// Arrange
		when(inboxRepository.findAll(any(Sort.class))).thenThrow(new RuntimeException("DB error"));

		// Act
		ResponseEntity<List<Inbox>> response = getInboxService.execute();

		// Assertions
		assertNotNull(response);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		// Verification
		verify(inboxRepository).findAll(any(Sort.class));
	}
}
