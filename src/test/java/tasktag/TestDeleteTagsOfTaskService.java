package test.java.tasktag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import main.io.github.trencmivront.dontforget.repos.TaskTagRepository;
import main.io.github.trencmivront.dontforget.services.tasktag.DeleteTagsOfTaskService;

@ExtendWith(MockitoExtension.class)
class TestDeleteTagsOfTaskService {

	@Mock
	private TaskTagRepository taskTagRepository;

	@InjectMocks
	private DeleteTagsOfTaskService deleteTagsOfTaskService;

	@Test
	void testServiceDeletesTagsSuccessfully() {
		// Mock behavior: deleteBytaskId is a void method, do nothing by default
		doNothing().when(taskTagRepository).deleteBytaskId(1L);

		// Call service method
		ResponseEntity<String> response = deleteTagsOfTaskService.execute(1L);

		// Assertions
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("TAGS OF TASK DELETED", response.getBody());

		// Verify interactions
		verify(taskTagRepository).deleteBytaskId(1L);
	}

	@Test
	void testServiceReturnsInternalServerErrorOnException() {
		// Mock exception
		doThrow(new RuntimeException("DB error")).when(taskTagRepository).deleteBytaskId(1L);

		// Call service method
		ResponseEntity<String> response = deleteTagsOfTaskService.execute(1L);

		// Assertions
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("FAILED TO DELETE TAGS OF TASK", response.getBody());

		// Verify interactions
		verify(taskTagRepository).deleteBytaskId(1L);
	}
}
