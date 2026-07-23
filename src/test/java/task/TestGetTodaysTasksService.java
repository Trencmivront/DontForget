package test.java.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import main.java.entities.Task;
import main.java.repos.TaskRepository;
import main.java.services.task.GetTodaysTasksService;

@ExtendWith(MockitoExtension.class)
class TestGetTodaysTasksService {

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private GetTodaysTasksService getTodaysTasksService;

	private Task sampleTask;

	@BeforeEach
	void setUp() {
		sampleTask = new Task();
		sampleTask.setTaskId(1L);
		sampleTask.setTaskTitle("Today's Task");
		sampleTask.setDueDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
	}

	@Test
	void testServiceReturnsTrueValue() {
		List<Task> expectedTasks = List.of(sampleTask);
		when(taskRepository.findTodaysTasks()).thenReturn(expectedTasks);

		ResponseEntity<List<Task>> response = getTodaysTasksService.execute();
		List<Task> actualTasks = response.getBody();

		assertNotNull(actualTasks);
		assertEquals(1, actualTasks.size());
		assertEquals("Today's Task", actualTasks.get(0).getTaskTitle());
		verify(taskRepository).findTodaysTasks();
	}

	@Test
	void testExecuteReturnsEmptyListOnException() {
		when(taskRepository.findTodaysTasks()).thenThrow(new RuntimeException("Database error"));

		ResponseEntity<List<Task>> response = getTodaysTasksService.execute();
		List<Task> actualTasks = response.getBody();

		assertNotNull(actualTasks);
		assertTrue(actualTasks.isEmpty());
		verify(taskRepository).findTodaysTasks();
	}
}
