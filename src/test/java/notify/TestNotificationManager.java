package test.java.notify;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import main.java.controllers.RecurringTaskController;
import main.java.controllers.ReminderController;
import main.java.controllers.TaskController;
import main.java.custom.SpringContext;
import main.java.dto.ReminderDTO;
import main.java.dto.TaskDTO;
import main.java.notify.NotificationManager;
import main.java.services.reminder.GetReminderByIdService;

@ExtendWith(MockitoExtension.class)
class TestNotificationManager {

	NotificationManager manager;

	// @Mock creates true Mockito mocks whose behaviour can be stubbed with when()
	@Mock
	private ReminderController reminderController;
	@Mock
	private TaskController taskController;
	@Mock
	private RecurringTaskController recurringTaskController;

	ReminderDTO testReminder, testNextReminder;
	TaskDTO testTask;
	List<DayOfWeek> testDayOfWeeks;

	// Holds the static mock so it can be closed after each test
	MockedStatic<SpringContext> springContextMock;

	@BeforeEach
	void init() {
		// Mock SpringContext.getBean() before NotificationManager is constructed,
		// because its fields are initialised via getBean() at construction time.
		springContextMock = Mockito.mockStatic(SpringContext.class);
		springContextMock.when(() -> SpringContext.getBean(ReminderController.class)).thenReturn(reminderController);
		springContextMock.when(() -> SpringContext.getBean(TaskController.class)).thenReturn(taskController);
		springContextMock.when(() -> SpringContext.getBean(RecurringTaskController.class)).thenReturn(recurringTaskController);

		manager = new NotificationManager();

		LocalDateTime pastDateTime = LocalDateTime.now().minusMinutes(1);
		LocalDateTime nextDateTime = pastDateTime.plusDays(1L);

		testReminder = new ReminderDTO(1L, pastDateTime, "Hello World");
		testNextReminder = new ReminderDTO(testReminder.getTaskId(),
				nextDateTime,
				testReminder.getMessage());

		testDayOfWeeks = List.of(pastDateTime.getDayOfWeek(), nextDateTime.getDayOfWeek());

		testTask = new TaskDTO();
		testTask.setTaskTitle("H");
		testTask.setDescription("ada");
	}

	@AfterEach
	void tearDown() {
		springContextMock.close();
	}

	@Test
	void testManagerSetsNextDayForReminder() {
		when(recurringTaskController.getRecurringDaysOfTask(1L)).thenReturn(ResponseEntity.ok(testDayOfWeeks));
		when(taskController.getTaskById(1L)).thenReturn(ResponseEntity.ok(testTask));
		when(reminderController.updateReminder(testReminder)).thenReturn(ResponseEntity.ok("REMINDER UPDATED"));
		
		manager.scheduleReminder(testReminder);

		assertEquals(testNextReminder, testReminder);
	}

}
