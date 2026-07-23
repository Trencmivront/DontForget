package test.java.inbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import main.java.repos.InboxRepository;
import main.java.services.inbox.DeleteMessageByIdService;

@ExtendWith(MockitoExtension.class)
class TestDeleteMessageByIdService {

	@Mock
	private InboxRepository inboxRepository;

	@InjectMocks
	private DeleteMessageByIdService deleteMessageByIdService;

	@Test
	void testServiceReturnsNotFoundWhenMessageDoesNotExist() {
		// Arrange
		Long id = 123L;
		when(inboxRepository.existsById(id)).thenReturn(false);

		// Act
		ResponseEntity<String> response = deleteMessageByIdService.execute(id);

		// Assertions
		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("MESSAGE NOT FOUND", response.getBody());

		// Verification
		verify(inboxRepository).existsById(id);
		verify(inboxRepository, never()).deleteById(id);
	}

	@Test
	void testServiceDeletesMessageSuccessfully() {
		// Arrange
		Long id = 123L;
		when(inboxRepository.existsById(id)).thenReturn(true);

		// Act
		ResponseEntity<String> response = deleteMessageByIdService.execute(id);

		// Assertions
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("MESSAGE DELETED", response.getBody());

		// Verification
		verify(inboxRepository).existsById(id);
		verify(inboxRepository).deleteById(id);
	}

	@Test
	void testServiceReturnsInternalServerErrorOnException() {
		// Arrange
		Long id = 123L;
		when(inboxRepository.existsById(id)).thenReturn(true);
		doThrow(new RuntimeException("DB error")).when(inboxRepository).deleteById(id);

		// Act
		ResponseEntity<String> response = deleteMessageByIdService.execute(id);

		// Assertions
		assertNotNull(response);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("FAILED TO DELETE MESSAGE", response.getBody());

		// Verification
		verify(inboxRepository).existsById(id);
		verify(inboxRepository).deleteById(id);
	}
}
